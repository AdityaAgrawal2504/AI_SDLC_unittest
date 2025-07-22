package com.example.chat.v1.service;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.enums.ErrorEventCodeC1V1;
import com.example.chat.v1.exception.ChatValidationExceptionC1V1;
import com.example.chat.v1.grpc.MarkAsSeenRequest;
import com.example.chat.v1.grpc.MessageStatus;
import com.example.chat.v1.grpc.SendMessageRequest;
import com.example.chat.v1.util.IdGeneratorC1V1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatLogicServiceC1V1Test {

    @Mock
    private MockDataRepositoryC1V1 dataRepository;
    @Mock
    private EventDispatchServiceC1V1 eventDispatchService;
    @Mock
    private IdGeneratorC1V1 idGenerator;

    @InjectMocks
    private ChatLogicServiceC1V1 chatLogicService;

    private String userId;
    private String conversationId;
    private UserC1V1 senderUser;
    private SendMessageRequest sendMessageRequest;
    private MarkAsSeenRequest markAsSeenRequest;
    private MessageC1V1 existingMessage;

    @BeforeEach
    void setUp() {
        userId = "user_123";
        conversationId = "conv_abc";
        senderUser = new UserC1V1(userId, "Test User", "avatar.png");

        sendMessageRequest = SendMessageRequest.newBuilder()
                .setClientMessageId(UUID.randomUUID().toString())
                .setConversationId(conversationId)
                .setContent("Hello World")
                .build();

        markAsSeenRequest = MarkAsSeenRequest.newBuilder()
                .setConversationId(conversationId)
                .setLastSeenMessageId("msg_seen123")
                .build();

        existingMessage = MessageC1V1.builder()
                .id("msg_exists")
                .clientMessageId(sendMessageRequest.getClientMessageId())
                .conversationId(conversationId)
                .senderId(userId)
                .content("Existing message")
                .createdAt(Instant.now())
                .status(MessageStatus.SENT)
                .build();

        // Default mock behaviors
        when(dataRepository.conversationExists(conversationId)).thenReturn(true);
        when(dataRepository.isUserMemberOfConversation(userId, conversationId)).thenReturn(true);
        when(dataRepository.findUserById(userId)).thenReturn(Optional.of(senderUser));
        when(idGenerator.newMessageId()).thenReturn("new_message_id");
    }

    // --- processSendMessage tests ---

    @Test
    void processSendMessage_Success() {
        // Arrange
        when(dataRepository.findMessageByClientMessageId(sendMessageRequest.getClientMessageId())).thenReturn(Optional.empty());
        when(dataRepository.saveMessage(any(MessageC1V1.class))).thenReturn(any(MessageC1V1.class));
        doNothing().when(eventDispatchService).dispatchNewMessage(any(MessageC1V1.class), any(UserC1V1.class));

        // Act
        chatLogicService.processSendMessage(sendMessageRequest, userId);

        // Assert
        verify(dataRepository, times(1)).findMessageByClientMessageId(sendMessageRequest.getClientMessageId());
        verify(dataRepository, times(1)).conversationExists(conversationId);
        verify(dataRepository, times(1)).isUserMemberOfConversation(userId, conversationId);
        verify(dataRepository, times(1)).findUserById(userId);
        verify(dataRepository, times(1)).saveMessage(argThat(msg ->
                msg.getClientMessageId().equals(sendMessageRequest.getClientMessageId()) &&
                msg.getConversationId().equals(sendMessageRequest.getConversationId()) &&
                msg.getContent().equals(sendMessageRequest.getContent()) &&
                msg.getSenderId().equals(userId) &&
                msg.getStatus().equals(MessageStatus.SENT)
        ));
        verify(eventDispatchService, times(1)).dispatchNewMessage(any(MessageC1V1.class), eq(senderUser));
    }

    @Test
    void processSendMessage_DuplicateClientMessageId_ThrowsIdempotencyKeyReplayException() {
        // Arrange
        when(dataRepository.findMessageByClientMessageId(sendMessageRequest.getClientMessageId())).thenReturn(Optional.of(existingMessage));

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processSendMessage(sendMessageRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.IDEMPOTENCY_KEY_REPLAY, exception.getErrorCode());
        assertEquals("Duplicate clientMessageId", exception.getMessage());
        assertEquals(sendMessageRequest.getClientMessageId(), exception.getClientMessageId());
        assertEquals("SendMessageRequest", exception.getOriginalRequestType());
        verify(dataRepository, never()).conversationExists(anyString());
        verify(dataRepository, never()).saveMessage(any());
        verify(eventDispatchService, never()).dispatchNewMessage(any(), any());
    }

    @Test
    void processSendMessage_ConversationNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(dataRepository.findMessageByClientMessageId(sendMessageRequest.getClientMessageId())).thenReturn(Optional.empty());
        when(dataRepository.conversationExists(conversationId)).thenReturn(false);

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processSendMessage(sendMessageRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals("Conversation not found", exception.getMessage());
        verify(dataRepository, never()).isUserMemberOfConversation(anyString(), anyString());
        verify(dataRepository, never()).saveMessage(any());
    }

    @Test
    void processSendMessage_UserNotMemberOfConversation_ThrowsForbiddenActionException() {
        // Arrange
        when(dataRepository.findMessageByClientMessageId(sendMessageRequest.getClientMessageId())).thenReturn(Optional.empty());
        when(dataRepository.isUserMemberOfConversation(userId, conversationId)).thenReturn(false);

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processSendMessage(sendMessageRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.FORBIDDEN_ACTION, exception.getErrorCode());
        assertEquals("User not a member of this conversation", exception.getMessage());
        verify(dataRepository, never()).saveMessage(any());
        verify(eventDispatchService, never()).dispatchNewMessage(any(), any());
    }

    @Test
    void processSendMessage_SenderUserNotFound_ThrowsUnknownErrorException() {
        // Arrange
        when(dataRepository.findMessageByClientMessageId(sendMessageRequest.getClientMessageId())).thenReturn(Optional.empty());
        when(dataRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processSendMessage(sendMessageRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.UNKNOWN_ERROR, exception.getErrorCode());
        assertEquals("Sender user not found, authentication issue.", exception.getMessage());
        verify(dataRepository, never()).saveMessage(any());
    }

    // --- processMarkAsSeen tests ---

    @Test
    void processMarkAsSeen_Success() {
        // Arrange
        MessageC1V1 lastSeenMsg = MessageC1V1.builder().id(markAsSeenRequest.getLastSeenMessageId()).conversationId(conversationId).build();
        when(dataRepository.findMessageById(markAsSeenRequest.getLastSeenMessageId())).thenReturn(Optional.of(lastSeenMsg));
        doNothing().when(eventDispatchService).dispatchSeenEvent(anyString(), anyString(), any(UserC1V1.class));

        // Act
        chatLogicService.processMarkAsSeen(markAsSeenRequest, userId);

        // Assert
        verify(dataRepository, times(1)).conversationExists(conversationId);
        verify(dataRepository, times(1)).isUserMemberOfConversation(userId, conversationId);
        verify(dataRepository, times(1)).findMessageById(markAsSeenRequest.getLastSeenMessageId());
        verify(dataRepository, times(1)).findUserById(userId);
        verify(eventDispatchService, times(1)).dispatchSeenEvent(conversationId, markAsSeenRequest.getLastSeenMessageId(), senderUser);
    }

    @Test
    void processMarkAsSeen_ConversationNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(dataRepository.conversationExists(conversationId)).thenReturn(false);

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processMarkAsSeen(markAsSeenRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals("Conversation not found", exception.getMessage());
        verify(dataRepository, never()).isUserMemberOfConversation(anyString(), anyString());
        verify(eventDispatchService, never()).dispatchSeenEvent(anyString(), anyString(), any());
    }

    @Test
    void processMarkAsSeen_UserNotMemberOfConversation_ThrowsForbiddenActionException() {
        // Arrange
        when(dataRepository.isUserMemberOfConversation(userId, conversationId)).thenReturn(false);

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processMarkAsSeen(markAsSeenRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.FORBIDDEN_ACTION, exception.getErrorCode());
        assertEquals("User not a member of this conversation", exception.getMessage());
        verify(eventDispatchService, never()).dispatchSeenEvent(anyString(), anyString(), any());
    }

    @Test
    void processMarkAsSeen_LastSeenMessageNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(dataRepository.findMessageById(markAsSeenRequest.getLastSeenMessageId())).thenReturn(Optional.empty());

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processMarkAsSeen(markAsSeenRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals("Last seen message not found or not in this conversation", exception.getMessage());
        verify(eventDispatchService, never()).dispatchSeenEvent(anyString(), anyString(), any());
    }
    
    @Test
    void processMarkAsSeen_LastSeenMessageNotInConversation_ThrowsResourceNotFoundException() {
        // Arrange
        MessageC1V1 lastSeenMsg = MessageC1V1.builder().id(markAsSeenRequest.getLastSeenMessageId()).conversationId("other_conv").build();
        when(dataRepository.findMessageById(markAsSeenRequest.getLastSeenMessageId())).thenReturn(Optional.of(lastSeenMsg));

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processMarkAsSeen(markAsSeenRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals("Last seen message not found or not in this conversation", exception.getMessage());
        verify(eventDispatchService, never()).dispatchSeenEvent(anyString(), anyString(), any());
    }

    @Test
    void processMarkAsSeen_SeenByUserNotFound_ThrowsUnknownErrorException() {
        // Arrange
        MessageC1V1 lastSeenMsg = MessageC1V1.builder().id(markAsSeenRequest.getLastSeenMessageId()).conversationId(conversationId).build();
        when(dataRepository.findMessageById(markAsSeenRequest.getLastSeenMessageId())).thenReturn(Optional.of(lastSeenMsg));
        when(dataRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                chatLogicService.processMarkAsSeen(markAsSeenRequest, userId)
        );

        assertEquals(ErrorEventCodeC1V1.UNKNOWN_ERROR, exception.getErrorCode());
        assertEquals("Seen by user not found, authentication issue.", exception.getMessage());
        verify(eventDispatchService, never()).dispatchSeenEvent(anyString(), anyString(), any());
    }

    // --- processTyping tests ---

    @Test
    void processTyping_IsTyping_Success() {
        // Arrange
        doNothing().when(eventDispatchService).dispatchTypingEvent(conversationId, senderUser);

        // Act
        chatLogicService.processTyping(conversationId, userId, true);

        // Assert
        verify(dataRepository, times(1)).conversationExists(conversationId);
        verify(dataRepository, times(1)).isUserMemberOfConversation(userId, conversationId);
        verify(dataRepository, times(1)).findUserById(userId);
        verify(eventDispatchService, times(1)).dispatchTypingEvent(conversationId, senderUser);
    }

    @Test
    void processTyping_IsNotTyping_NoEventDispatched() {
        // Act
        chatLogicService.processTyping(conversationId, userId, false);

        // Assert
        verify(dataRepository, times(1)).conversationExists(conversationId);
        verify(dataRepository, times(1)).isUserMemberOfConversation(userId, conversationId);
        verify(dataRepository, times(1)).findUserById(userId); // still tries to find user
        verify(eventDispatchService, never()).dispatchTypingEvent(anyString(), any(UserC1V1.class));
    }

    @Test
    void processTyping_ConversationNotFound_NoEventDispatched() {
        // Arrange
        when(dataRepository.conversationExists(conversationId)).thenReturn(false);

        // Act
        chatLogicService.processTyping(conversationId, userId, true);

        // Assert
        verify(dataRepository, times(1)).conversationExists(conversationId);
        verify(dataRepository, never()).isUserMemberOfConversation(anyString(), anyString());
        verify(eventDispatchService, never()).dispatchTypingEvent(anyString(), any(UserC1V1.class));
    }

    @Test
    void processTyping_UserNotMemberOfConversation_NoEventDispatched() {
        // Arrange
        when(dataRepository.isUserMemberOfConversation(userId, conversationId)).thenReturn(false);

        // Act
        chatLogicService.processTyping(conversationId, userId, true);

        // Assert
        verify(dataRepository, times(1)).conversationExists(conversationId);
        verify(dataRepository, times(1)).isUserMemberOfConversation(userId, conversationId);
        verify(dataRepository, never()).findUserById(anyString()); // doesn't proceed to find user
        verify(eventDispatchService, never()).dispatchTypingEvent(anyString(), any(UserC1V1.class));
    }

    @Test
    void processTyping_TypingUserNotFound_NoEventDispatched() {
        // Arrange
        when(dataRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act
        chatLogicService.processTyping(conversationId, userId, true);

        // Assert
        verify(dataRepository, times(1)).conversationExists(conversationId);
        verify(dataRepository, times(1)).isUserMemberOfConversation(userId, conversationId);
        verify(dataRepository, times(1)).findUserById(userId);
        verify(eventDispatchService, never()).dispatchTypingEvent(anyString(), any(UserC1V1.class));
    }
}
```
```java
// src/test/java/com/example/chat/v1/service/EventDispatchServiceC1V1Test.java