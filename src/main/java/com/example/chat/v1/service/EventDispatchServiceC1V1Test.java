package com.example.chat.v1.service;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.grpc.Message;
import com.example.chat.v1.grpc.ServerToClientEvent;
import com.example.chat.v1.grpc.User;
import com.example.chat.v1.util.ProtoMapperC1V1;
import com.google.protobuf.Struct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventDispatchServiceC1V1Test {

    @Mock
    private UserStreamManagerC1V1 streamManager;
    @Mock
    private ProtoMapperC1V1 protoMapper;
    @Mock
    private MockDataRepositoryC1V1 dataRepository;

    @InjectMocks
    private EventDispatchServiceC1V1 eventDispatchService;

    private String conversationId;
    private UserC1V1 user1, user2, user3;
    private MessageC1V1 message;
    private Message protoMessage;
    private User protoUser1, protoUser2;

    @BeforeEach
    void setUp() {
        conversationId = "conv_abc";
        user1 = new UserC1V1("user_1", "Alice", "alice.png");
        user2 = new UserC1V1("user_2", "Bob", "bob.png");
        user3 = new UserC1V1("user_3", "Charlie", "charlie.png"); // Not in conversation by default

        message = MessageC1V1.builder()
                .id("msg_1")
                .clientMessageId("client_msg_1")
                .conversationId(conversationId)
                .senderId(user1.getId())
                .content("Hello")
                .createdAt(Instant.now())
                .build();

        protoUser1 = User.newBuilder().setId(user1.getId()).setDisplayName(user1.getDisplayName()).setAvatarUrl(user1.getAvatarUrl()).build();
        protoUser2 = User.newBuilder().setId(user2.getId()).setDisplayName(user2.getDisplayName()).setAvatarUrl(user2.getAvatarUrl()).build();
        protoMessage = Message.newBuilder()
                .setId(message.getId())
                .setClientMessageId(message.getClientMessageId())
                .setConversationId(message.getConversationId())
                .setSender(protoUser1)
                .setContent(message.getContent())
                .setCreatedAt(protoMapper.toProtoTimestamp(message.getCreatedAt()))
                .build();

        when(protoMapper.toProtoUser(user1)).thenReturn(protoUser1);
        when(protoMapper.toProtoUser(user2)).thenReturn(protoUser2);
        when(protoMapper.toProtoMessage(message, user1)).thenReturn(protoMessage);
        when(dataRepository.getConversationMemberIds(conversationId)).thenReturn(Arrays.asList(user1.getId(), user2.getId()));
    }

    @Test
    void dispatchNewMessage_SendsToAllConversationMembers() {
        // Act
        eventDispatchService.dispatchNewMessage(message, user1);

        // Assert
        ArgumentCaptor<ServerToClientEvent> eventCaptor = ArgumentCaptor.forClass(ServerToClientEvent.class);
        verify(streamManager, times(1)).sendToUser(eq(user1.getId()), eventCaptor.capture());
        verify(streamManager, times(1)).sendToUser(eq(user2.getId()), eventCaptor.capture());
        verify(streamManager, never()).sendToUser(eq(user3.getId()), any());

        List<ServerToClientEvent> capturedEvents = eventCaptor.getAllValues();
        assertEquals(2, capturedEvents.size());
        assertTrue(capturedEvents.stream().allMatch(e -> e.hasNewMessageEvent()));
        assertEquals(protoMessage, capturedEvents.get(0).getNewMessageEvent().getMessage()); // Events should be identical
        assertEquals(protoMessage, capturedEvents.get(1).getNewMessageEvent().getMessage());
    }

    @Test
    void dispatchTypingEvent_SendsToOtherConversationMembers() {
        // Act
        eventDispatchService.dispatchTypingEvent(conversationId, user1);

        // Assert
        ArgumentCaptor<ServerToClientEvent> eventCaptor = ArgumentCaptor.forClass(ServerToClientEvent.class);
        verify(streamManager, never()).sendToUser(eq(user1.getId()), any()); // Should not send to self
        verify(streamManager, times(1)).sendToUser(eq(user2.getId()), eventCaptor.capture());
        verify(streamManager, never()).sendToUser(eq(user3.getId()), any());

        ServerToClientEvent capturedEvent = eventCaptor.getValue();
        assertTrue(capturedEvent.hasUserTypingEvent());
        assertEquals(conversationId, capturedEvent.getUserTypingEvent().getConversationId());
        assertEquals(protoUser1, capturedEvent.getUserTypingEvent().getUser());
    }

    @Test
    void dispatchSeenEvent_SendsToAllConversationMembers() {
        // Arrange
        String lastSeenMessageId = "msg_last";
        when(protoMapper.toProtoTimestamp(any(Instant.class))).thenReturn(com.google.protobuf.Timestamp.getDefaultInstance()); // Mock timestamp

        // Act
        eventDispatchService.dispatchSeenEvent(conversationId, lastSeenMessageId, user1);

        // Assert
        ArgumentCaptor<ServerToClientEvent> eventCaptor = ArgumentCaptor.forClass(ServerToClientEvent.class);
        verify(streamManager, times(1)).sendToUser(eq(user1.getId()), eventCaptor.capture());
        verify(streamManager, times(1)).sendToUser(eq(user2.getId()), eventCaptor.capture());
        verify(streamManager, never()).sendToUser(eq(user3.getId()), any());

        List<ServerToClientEvent> capturedEvents = eventCaptor.getAllValues();
        assertEquals(2, capturedEvents.size());
        assertTrue(capturedEvents.stream().allMatch(e -> e.hasMessageSeenEvent()));
        assertEquals(conversationId, capturedEvents.get(0).getMessageSeenEvent().getConversationId());
        assertEquals(lastSeenMessageId, capturedEvents.get(0).getMessageSeenEvent().getLastSeenMessageId());
        assertEquals(user1.getId(), capturedEvents.get(0).getMessageSeenEvent().getSeenByUserId());
    }

    @Test
    void buildErrorEvent_WithClientMessageIdAndOriginalRequestType_ReturnsCorrectEvent() {
        // Arrange
        String errorCode = "VALIDATION_FAILED";
        String errorMessage = "Content too short";
        String clientMessageId = "some-uuid";
        String originalRequestType = "SendMessageRequest";

        // Act
        ServerToClientEvent errorEvent = eventDispatchService.buildErrorEvent(errorCode, errorMessage, clientMessageId, originalRequestType);

        // Assert
        assertTrue(errorEvent.hasErrorEvent());
        assertEquals(errorCode, errorEvent.getErrorEvent().getErrorCode());
        assertEquals(errorMessage, errorEvent.getErrorEvent().getErrorMessage());

        Struct details = errorEvent.getErrorEvent().getDetails();
        assertTrue(details.getFieldsMap().containsKey("clientMessageId"));
        assertEquals(clientMessageId, details.getFieldsMap().get("clientMessageId").getStringValue());
        assertTrue(details.getFieldsMap().containsKey("originalRequestType"));
        assertEquals(originalRequestType, details.getFieldsMap().get("originalRequestType").getStringValue());
        assertTrue(details.getFieldsMap().containsKey("serverTimestamp"));
        assertNotNull(details.getFieldsMap().get("serverTimestamp").getStringValue());
    }

    @Test
    void buildErrorEvent_WithoutClientMessageIdAndOriginalRequestType_ReturnsCorrectEvent() {
        // Arrange
        String errorCode = "UNKNOWN_ERROR";
        String errorMessage = "Internal server error";

        // Act
        ServerToClientEvent errorEvent = eventDispatchService.buildErrorEvent(errorCode, errorMessage, null, null);

        // Assert
        assertTrue(errorEvent.hasErrorEvent());
        assertEquals(errorCode, errorEvent.getErrorEvent().getErrorCode());
        assertEquals(errorMessage, errorEvent.getErrorEvent().getErrorMessage());

        Struct details = errorEvent.getErrorEvent().getDetails();
        assertFalse(details.getFieldsMap().containsKey("clientMessageId"));
        assertFalse(details.getFieldsMap().containsKey("originalRequestType"));
        assertTrue(details.getFieldsMap().containsKey("serverTimestamp"));
    }
}
```
```java
// src/test/java/com/example/chat/v1/service/MockDataRepositoryC1V1Test.java