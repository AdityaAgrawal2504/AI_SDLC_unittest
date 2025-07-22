package com.example.chat.v1.service;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.enums.ErrorEventCodeC1V1;
import com.example.chat.v1.exception.ChatValidationExceptionC1V1;
import com.example.chat.v1.grpc.MarkAsSeenRequest;
import com.example.chat.v1.grpc.MessageStatus;
import com.example.chat.v1.grpc.SendMessageRequest;
import com.example.chat.v1.logging.LoggableC1V1;
import com.example.chat.v1.util.IdGeneratorC1V1;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Handles the core business logic for chat operations.
 */
@Service
public class ChatLogicServiceC1V1 {

    private final MockDataRepositoryC1V1 dataRepository;
    private final EventDispatchServiceC1V1 eventDispatchService;
    private final IdGeneratorC1V1 idGenerator;

    public ChatLogicServiceC1V1(MockDataRepositoryC1V1 dataRepository, EventDispatchServiceC1V1 eventDispatchService, IdGeneratorC1V1 idGenerator) {
        this.dataRepository = dataRepository;
        this.eventDispatchService = eventDispatchService;
        this.idGenerator = idGenerator;
    }

    /**
     * Processes a request to send a new message.
     */
    @LoggableC1V1
    public void processSendMessage(SendMessageRequest request, String senderId) {
        // Idempotency Check
        Optional<MessageC1V1> existingMessage = dataRepository.findMessageByClientMessageId(request.getClientMessageId());
        if (existingMessage.isPresent()) {
            throw new ChatValidationExceptionC1V1("Duplicate clientMessageId", ErrorEventCodeC1V1.IDEMPOTENCY_KEY_REPLAY, request.getClientMessageId(), "SendMessageRequest");
        }
        
        // Conversation existence check
        if (!dataRepository.conversationExists(request.getConversationId())) {
            throw new ChatValidationExceptionC1V1("Conversation not found", ErrorEventCodeC1V1.RESOURCE_NOT_FOUND, request.getClientMessageId(), "SendMessageRequest");
        }

        // Authorization Check
        if (!dataRepository.isUserMemberOfConversation(senderId, request.getConversationId())) {
            throw new ChatValidationExceptionC1V1("User not a member of this conversation", ErrorEventCodeC1V1.FORBIDDEN_ACTION, request.getClientMessageId(), "SendMessageRequest");
        }

        UserC1V1 sender = dataRepository.findUserById(senderId)
                .orElseThrow(() -> new ChatValidationExceptionC1V1("Sender user not found, authentication issue.", ErrorEventCodeC1V1.UNKNOWN_ERROR, request.getClientMessageId(), "SendMessageRequest"));

        // Create and save message
        MessageC1V1 message = MessageC1V1.builder()
                .id(idGenerator.newMessageId())
                .clientMessageId(request.getClientMessageId())
                .conversationId(request.getConversationId())
                .senderId(senderId)
                .content(request.getContent())
                .createdAt(Instant.now())
                .status(MessageStatus.SENT) // Assume sent once it hits the server
                .build();
        dataRepository.saveMessage(message);

        // Dispatch event to members
        eventDispatchService.dispatchNewMessage(message, sender);
    }
    
    /**
     * Processes a request to mark messages as seen.
     */
    @LoggableC1V1
    public void processMarkAsSeen(MarkAsSeenRequest request, String userId) {
        if (!dataRepository.conversationExists(request.getConversationId())) {
            throw new ChatValidationExceptionC1V1("Conversation not found", ErrorEventCodeC1V1.RESOURCE_NOT_FOUND, null, "MarkAsSeenRequest");
        }
        
        if (!dataRepository.isUserMemberOfConversation(userId, request.getConversationId())) {
            throw new ChatValidationExceptionC1V1("User not a member of this conversation", ErrorEventCodeC1V1.FORBIDDEN_ACTION, null, "MarkAsSeenRequest");
        }

        // Validate lastSeenMessageId exists and belongs to the conversation
        Optional<MessageC1V1> lastSeenMessage = dataRepository.findMessageById(request.getLastSeenMessageId());
        if (lastSeenMessage.isEmpty() || !lastSeenMessage.get().getConversationId().equals(request.getConversationId())) {
             throw new ChatValidationExceptionC1V1("Last seen message not found or not in this conversation", ErrorEventCodeC1V1.RESOURCE_NOT_FOUND, null, "MarkAsSeenRequest");
        }

        UserC1V1 seenByUser = dataRepository.findUserById(userId)
                .orElseThrow(() -> new ChatValidationExceptionC1V1("Seen by user not found, authentication issue.", ErrorEventCodeC1V1.UNKNOWN_ERROR, null, "MarkAsSeenRequest"));

        // In a real app, you would update message statuses in the DB up to lastSeenMessageId.
        // For this example, we just broadcast the event.
        eventDispatchService.dispatchSeenEvent(request.getConversationId(), request.getLastSeenMessageId(), seenByUser);
    }

    /**
     * Processes a user typing event.
     */
    @LoggableC1V1
    public void processTyping(String conversationId, String userId, boolean isTyping) {
        if (!dataRepository.conversationExists(conversationId)) {
            // Silently fail, no need to send error for transient typing events
            return;
        }
        if (!dataRepository.isUserMemberOfConversation(userId, conversationId)) {
            // Silently fail, no need to send error for unauthorized typing events
            return;
        }
        
        UserC1V1 typingUser = dataRepository.findUserById(userId).orElse(null); // Should exist from auth interceptor, but handle defensively
        if (typingUser == null) {
            return; // Silently fail if user not found (e.g., deleted mid-stream)
        }

        // Stop typing is not an event in the spec, we only notify on start.
        if (isTyping) {
            eventDispatchService.dispatchTypingEvent(conversationId, typingUser);
        }
    }
}
```
```java
// src/main/java/com/example/chat/v1/grpc/AuthInterceptorC1V1.java