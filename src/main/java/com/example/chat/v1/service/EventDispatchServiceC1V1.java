package com.example.chat.v1.service;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.grpc.*;
import com.example.chat.v1.logging.LoggableC1V1;
import com.example.chat.v1.util.ProtoMapperC1V1;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for creating and dispatching server-to-client events.
 */
@Service
public class EventDispatchServiceC1V1 {

    private final UserStreamManagerC1V1 streamManager;
    private final ProtoMapperC1V1 protoMapper;
    private final MockDataRepositoryC1V1 dataRepository;

    public EventDispatchServiceC1V1(UserStreamManagerC1V1 streamManager, ProtoMapperC1V1 protoMapper, MockDataRepositoryC1V1 dataRepository) {
        this.streamManager = streamManager;
        this.protoMapper = protoMapper;
        this.dataRepository = dataRepository;
    }

    /**
     * Dispatches a new message event to all members of a conversation.
     */
    @LoggableC1V1
    public void dispatchNewMessage(MessageC1V1 message, UserC1V1 sender) {
        List<String> memberIds = dataRepository.getConversationMemberIds(message.getConversationId());
        NewMessageEvent eventPayload = NewMessageEvent.newBuilder()
                .setMessage(protoMapper.toProtoMessage(message, sender))
                .build();
        ServerToClientEvent event = ServerToClientEvent.newBuilder()
                .setNewMessageEvent(eventPayload)
                .build();

        for (String memberId : memberIds) {
            streamManager.sendToUser(memberId, event);
        }
    }

    /**
     * Dispatches a typing event to conversation members.
     */
    @LoggableC1V1
    public void dispatchTypingEvent(String conversationId, UserC1V1 typingUser) {
        List<String> memberIds = dataRepository.getConversationMemberIds(conversationId);
        UserTypingEvent eventPayload = UserTypingEvent.newBuilder()
                .setConversationId(conversationId)
                .setUser(protoMapper.toProtoUser(typingUser))
                .build();
        ServerToClientEvent event = ServerToClientEvent.newBuilder()
                .setUserTypingEvent(eventPayload)
                .build();
        
        // Send typing notification to all members *except* the typing user
        for (String memberId : memberIds) {
            if (!memberId.equals(typingUser.getId())) { 
                streamManager.sendToUser(memberId, event);
            }
        }
    }

    /**
     * Dispatches a seen event to all members of a conversation.
     */
    @LoggableC1V1
    public void dispatchSeenEvent(String conversationId, String lastSeenMessageId, UserC1V1 seenByUser) {
        List<String> memberIds = dataRepository.getConversationMemberIds(conversationId);
        MessageSeenEvent eventPayload = MessageSeenEvent.newBuilder()
                .setConversationId(conversationId)
                .setLastSeenMessageId(lastSeenMessageId)
                .setSeenByUserId(seenByUser.getId())
                .setSeenAt(protoMapper.toProtoTimestamp(Instant.now()))
                .build();
        ServerToClientEvent event = ServerToClientEvent.newBuilder()
                .setMessageSeenEvent(eventPayload)
                .build();
        
        for (String memberId : memberIds) {
            streamManager.sendToUser(memberId, event);
        }
    }
    
    /**
     * Builds and returns an ErrorEvent protobuf message.
     */
    public ServerToClientEvent buildErrorEvent(String errorCode, String errorMessage, String clientMessageId, String originalRequestType) {
        Struct.Builder detailsBuilder = Struct.newBuilder();
        if (clientMessageId != null) {
            detailsBuilder.putFields("clientMessageId", Value.newBuilder().setStringValue(clientMessageId).build());
        }
        if (originalRequestType != null) {
            detailsBuilder.putFields("originalRequestType", Value.newBuilder().setStringValue(originalRequestType).build());
        }
        
        // Add current timestamp to error details for debugging
        detailsBuilder.putFields("serverTimestamp", Value.newBuilder().setStringValue(Instant.now().toString()).build());

        ErrorEvent errorEvent = ErrorEvent.newBuilder()
                .setErrorCode(errorCode)
                .setErrorMessage(errorMessage)
                .setDetails(detailsBuilder)
                .build();

        return ServerToClientEvent.newBuilder().setErrorEvent(errorEvent).build();
    }
}
```
```java
// src/main/java/com/example/chat/v1/service/ChatLogicServiceC1V1.java