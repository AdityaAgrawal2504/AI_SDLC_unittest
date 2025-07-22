package com.example.chat.v1.validation;

import com.example.chat.v1.enums.ErrorEventCodeC1V1;
import com.example.chat.v1.exception.ChatValidationExceptionC1V1;
import com.example.chat.v1.grpc.ClientToServerEvent;
import com.example.chat.v1.grpc.MarkAsSeenRequest;
import com.example.chat.v1.grpc.SendMessageRequest;
import com.example.chat.v1.grpc.StartTypingRequest;
import com.example.chat.v1.grpc.StopTypingRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.example.chat.v1.util.ConstantsC1V1.*;

/**
 * Validates incoming client request messages.
 */
@Component
public class RequestValidatorC1V1 {

    /**
     * Validates a ClientToServerEvent wrapper message.
     */
    public void validate(ClientToServerEvent event) {
        if (event == null || event.getEventCase() == ClientToServerEvent.EventCase.EVENT_NOT_SET) {
            throw new ChatValidationExceptionC1V1("Unknown or empty event type", ErrorEventCodeC1V1.VALIDATION_FAILED);
        }
        switch (event.getEventCase()) {
            case SEND_MESSAGE_REQUEST:
                validateSendMessage(event.getSendMessageRequest());
                break;
            case MARK_AS_SEEN_REQUEST:
                validateMarkAsSeen(event.getMarkAsSeenRequest());
                break;
            case START_TYPING_REQUEST:
                validateStartTyping(event.getStartTypingRequest());
                break;
            case STOP_TYPING_REQUEST:
                validateStopTyping(event.getStopTypingRequest());
                break;
            default:
                throw new ChatValidationExceptionC1V1("Unknown or unhandled event type: " + event.getEventCase(), ErrorEventCodeC1V1.VALIDATION_FAILED);
        }
    }

    private void validateSendMessage(SendMessageRequest request) {
        String clientMessageId = request.getClientMessageId();
        String originalRequestType = "SendMessageRequest";
        
        validateRequiredString(clientMessageId, "clientMessageId", clientMessageId, originalRequestType);
        validatePattern(clientMessageId, UUID_V4_PATTERN, "clientMessageId", clientMessageId, originalRequestType);

        validateRequiredString(request.getConversationId(), "conversationId", clientMessageId, originalRequestType);
        validatePattern(request.getConversationId(), CONVERSATION_ID_PATTERN, "conversationId", clientMessageId, originalRequestType);

        validateRequiredString(request.getContent(), "content", clientMessageId, originalRequestType);
        validateLength(request.getContent(), MESSAGE_CONTENT_MIN_LENGTH, MESSAGE_CONTENT_MAX_LENGTH, "content", clientMessageId, originalRequestType);
    }

    private void validateMarkAsSeen(MarkAsSeenRequest request) {
        String originalRequestType = "MarkAsSeenRequest";
        // clientMessageId is not present in MarkAsSeenRequest, pass null
        validateRequiredString(request.getConversationId(), "conversationId", null, originalRequestType);
        validatePattern(request.getConversationId(), CONVERSATION_ID_PATTERN, "conversationId", null, originalRequestType);

        validateRequiredString(request.getLastSeenMessageId(), "lastSeenMessageId", null, originalRequestType);
        validatePattern(request.getLastSeenMessageId(), MESSAGE_ID_PATTERN, "lastSeenMessageId", null, originalRequestType);
    }
    
    private void validateStartTyping(StartTypingRequest request) {
        String originalRequestType = "StartTypingRequest";
        validateRequiredString(request.getConversationId(), "conversationId", null, originalRequestType);
        validatePattern(request.getConversationId(), CONVERSATION_ID_PATTERN, "conversationId", null, originalRequestType);
    }

    private void validateStopTyping(StopTypingRequest request) {
        String originalRequestType = "StopTypingRequest";
        validateRequiredString(request.getConversationId(), "conversationId", null, originalRequestType);
        validatePattern(request.getConversationId(), CONVERSATION_ID_PATTERN, "conversationId", null, originalRequestType);
    }

    private void validateRequiredString(String value, String fieldName, String clientMessageId, String requestType) {
        if (!StringUtils.hasText(value)) {
            throw new ChatValidationExceptionC1V1(fieldName + " is required.", ErrorEventCodeC1V1.VALIDATION_FAILED, clientMessageId, requestType);
        }
    }

    private void validatePattern(String value, String pattern, String fieldName, String clientMessageId, String requestType) {
        if (!value.matches(pattern)) {
            throw new ChatValidationExceptionC1V1("Invalid format for " + fieldName, ErrorEventCodeC1V1.VALIDATION_FAILED, clientMessageId, requestType);
        }
    }

    private void validateLength(String value, int min, int max, String fieldName, String clientMessageId, String requestType) {
        if (value.length() < min || value.length() > max) {
            throw new ChatValidationExceptionC1V1(fieldName + " length must be between " + min + " and " + max, ErrorEventCodeC1V1.VALIDATION_FAILED, clientMessageId, requestType);
        }
    }
}
```
```java
// src/main/java/com/example/chat/v1/service/MockDataRepositoryC1V1.java