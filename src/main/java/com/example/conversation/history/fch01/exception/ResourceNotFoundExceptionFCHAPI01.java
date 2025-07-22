package com.example.conversation.history.fch01.exception;

import com.example.conversation.history.fch01.enums.ErrorCodeFCHAPI01;
import lombok.Getter;

/**
 * Custom exception for handling 404 Not Found errors.
 */
@Getter
public class ResourceNotFoundExceptionFCHAPI01 extends RuntimeException {
    private final ErrorCodeFCHAPI01 errorCode = ErrorCodeFCHAPI01.CONVERSATION_NOT_FOUND;

    public ResourceNotFoundExceptionFCHAPI01(String message) {
        super(message);
    }
}
src/main/java/com/example/conversation/history/fch01/logging/StructuredLoggerFCHAPI01.java