package com.example.conversation.history.fch01.exception;

import com.example.conversation.history.fch01.enums.ErrorCodeFCHAPI01;
import lombok.Getter;

/**
 * Custom exception for handling 403 Forbidden errors.
 */
@Getter
public class PermissionDeniedExceptionFCHAPI01 extends RuntimeException {
    private final ErrorCodeFCHAPI01 errorCode = ErrorCodeFCHAPI01.PERMISSION_DENIED;

    public PermissionDeniedExceptionFCHAPI01(String message) {
        super(message);
    }
}
src/main/java/com/example/conversation/history/fch01/exception/ResourceNotFoundExceptionFCHAPI01.java