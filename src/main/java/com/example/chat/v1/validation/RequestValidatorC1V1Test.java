package com.example.chat.v1.validation;

import com.example.chat.v1.enums.ErrorEventCodeC1V1;
import com.example.chat.v1.exception.ChatValidationExceptionC1V1;
import com.example.chat.v1.grpc.ClientToServerEvent;
import com.example.chat.v1.grpc.MarkAsSeenRequest;
import com.example.chat.v1.grpc.SendMessageRequest;
import com.example.chat.v1.grpc.StartTypingRequest;
import com.example.chat.v1.grpc.StopTypingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.example.chat.v1.util.ConstantsC1V1.MESSAGE_CONTENT_MAX_LENGTH;
import static com.example.chat.v1.util.ConstantsC1V1.MESSAGE_CONTENT_MIN_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class RequestValidatorC1V1Test {

    @InjectMocks
    private RequestValidatorC1V1 requestValidator;

    private String validUuid;
    private String validConversationId;
    private String validMessageId;
    private String validContent;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        validConversationId = "conv_abcdefghijklmnopqrstuvwx";
        validMessageId = "msg_abcdefghijklmnopqrstuvwx";
        validContent = "This is a valid message content.";
    }

    // --- General Validation Tests ---

    @Test
    void validate_NullEvent_ThrowsValidationFailed() {
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(null)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Unknown or empty event type", exception.getMessage());
    }

    @Test
    void validate_EventNotSet_ThrowsValidationFailed() {
        ClientToServerEvent emptyEvent = ClientToServerEvent.newBuilder().build();
        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(emptyEvent)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Unknown or empty event type", exception.getMessage());
    }

    // --- SendMessageRequest Validation Tests ---

    @Test
    void validateSendMessage_ValidRequest_NoException() {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setClientMessageId(validUuid)
                .setConversationId(validConversationId)
                .setContent(validContent)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        assertDoesNotThrow(() -> requestValidator.validate(event));
    }

    @Test
    void validateSendMessage_MissingClientMessageId_ThrowsValidationFailed() {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setConversationId(validConversationId)
                .setContent(validContent)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("clientMessageId is required.", exception.getMessage());
        assertEquals(request.getClientMessageId(), exception.getClientMessageId()); // Should be empty string
        assertEquals("SendMessageRequest", exception.getOriginalRequestType());
    }

    @Test
    void validateSendMessage_InvalidClientMessageIdFormat_ThrowsValidationFailed() {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setClientMessageId("invalid-uuid-format")
                .setConversationId(validConversationId)
                .setContent(validContent)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Invalid format for clientMessageId", exception.getMessage());
    }

    @Test
    void validateSendMessage_MissingConversationId_ThrowsValidationFailed() {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setClientMessageId(validUuid)
                .setContent(validContent)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("conversationId is required.", exception.getMessage());
    }

    @Test
    void validateSendMessage_InvalidConversationIdFormat_ThrowsValidationFailed() {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setClientMessageId(validUuid)
                .setConversationId("invalid_conv_id")
                .setContent(validContent)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Invalid format for conversationId", exception.getMessage());
    }

    @Test
    void validateSendMessage_MissingContent_ThrowsValidationFailed() {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setClientMessageId(validUuid)
                .setConversationId(validConversationId)
                .build(); // Content is empty string by default
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("content is required.", exception.getMessage());
    }

    @Test
    void validateSendMessage_ContentTooShort_ThrowsValidationFailed() {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setClientMessageId(validUuid)
                .setConversationId(validConversationId)
                .setContent("") // Min length is 1
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("content is required.", exception.getMessage()); // Fails on required check first
    }

    @Test
    void validateSendMessage_ContentTooLong_ThrowsValidationFailed() {
        String longContent = "a".repeat(MESSAGE_CONTENT_MAX_LENGTH + 1); // Max length is 10000
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setClientMessageId(validUuid)
                .setConversationId(validConversationId)
                .setContent(longContent)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("content length must be between " + MESSAGE_CONTENT_MIN_LENGTH + " and " + MESSAGE_CONTENT_MAX_LENGTH, exception.getMessage());
    }


    // --- MarkAsSeenRequest Validation Tests ---

    @Test
    void validateMarkAsSeen_ValidRequest_NoException() {
        MarkAsSeenRequest request = MarkAsSeenRequest.newBuilder()
                .setConversationId(validConversationId)
                .setLastSeenMessageId(validMessageId)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setMarkAsSeenRequest(request).build();

        assertDoesNotThrow(() -> requestValidator.validate(event));
    }

    @Test
    void validateMarkAsSeen_MissingConversationId_ThrowsValidationFailed() {
        MarkAsSeenRequest request = MarkAsSeenRequest.newBuilder()
                .setLastSeenMessageId(validMessageId)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setMarkAsSeenRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("conversationId is required.", exception.getMessage());
    }

    @Test
    void validateMarkAsSeen_InvalidConversationIdFormat_ThrowsValidationFailed() {
        MarkAsSeenRequest request = MarkAsSeenRequest.newBuilder()
                .setConversationId("invalid_conv")
                .setLastSeenMessageId(validMessageId)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setMarkAsSeenRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Invalid format for conversationId", exception.getMessage());
    }

    @Test
    void validateMarkAsSeen_MissingLastSeenMessageId_ThrowsValidationFailed() {
        MarkAsSeenRequest request = MarkAsSeenRequest.newBuilder()
                .setConversationId(validConversationId)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setMarkAsSeenRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("lastSeenMessageId is required.", exception.getMessage());
    }

    @Test
    void validateMarkAsSeen_InvalidLastSeenMessageIdFormat_ThrowsValidationFailed() {
        MarkAsSeenRequest request = MarkAsSeenRequest.newBuilder()
                .setConversationId(validConversationId)
                .setLastSeenMessageId("invalid_msg")
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setMarkAsSeenRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Invalid format for lastSeenMessageId", exception.getMessage());
    }

    // --- StartTypingRequest Validation Tests ---

    @Test
    void validateStartTyping_ValidRequest_NoException() {
        StartTypingRequest request = StartTypingRequest.newBuilder()
                .setConversationId(validConversationId)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStartTypingRequest(request).build();

        assertDoesNotThrow(() -> requestValidator.validate(event));
    }

    @Test
    void validateStartTyping_MissingConversationId_ThrowsValidationFailed() {
        StartTypingRequest request = StartTypingRequest.newBuilder().build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStartTypingRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("conversationId is required.", exception.getMessage());
    }

    @Test
    void validateStartTyping_InvalidConversationIdFormat_ThrowsValidationFailed() {
        StartTypingRequest request = StartTypingRequest.newBuilder()
                .setConversationId("invalid_conv")
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStartTypingRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Invalid format for conversationId", exception.getMessage());
    }

    // --- StopTypingRequest Validation Tests ---

    @Test
    void validateStopTyping_ValidRequest_NoException() {
        StopTypingRequest request = StopTypingRequest.newBuilder()
                .setConversationId(validConversationId)
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStopTypingRequest(request).build();

        assertDoesNotThrow(() -> requestValidator.validate(event));
    }

    @Test
    void validateStopTyping_MissingConversationId_ThrowsValidationFailed() {
        StopTypingRequest request = StopTypingRequest.newBuilder().build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStopTypingRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("conversationId is required.", exception.getMessage());
    }

    @Test
    void validateStopTyping_InvalidConversationIdFormat_ThrowsValidationFailed() {
        StopTypingRequest request = StopTypingRequest.newBuilder()
                .setConversationId("invalid_conv")
                .build();
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStopTypingRequest(request).build();

        ChatValidationExceptionC1V1 exception = assertThrows(ChatValidationExceptionC1V1.class, () ->
                requestValidator.validate(event)
        );
        assertEquals(ErrorEventCodeC1V1.VALIDATION_FAILED, exception.getErrorCode());
        assertEquals("Invalid format for conversationId", exception.getMessage());
    }
}
```
```java
// src/test/java/com/example/chat/v1/logging/Log4j2LoggerC1V1Test.java