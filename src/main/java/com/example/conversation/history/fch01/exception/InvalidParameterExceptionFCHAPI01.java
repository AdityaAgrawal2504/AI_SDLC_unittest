package com.example.conversation.history.fch01.exception;

import com.example.conversation.history.fch01.dto.ValidationErrorDetailFCHAPI01;
import lombok.Getter;
import java.util.List;

/**
 * Custom exception for handling 400 Bad Request errors with validation details.
 */
@Getter
public class InvalidParameterExceptionFCHAPI01 extends RuntimeException {
    private final List<ValidationErrorDetailFCHAPI01> details;

    public InvalidParameterExceptionFCHAPI01(String message, List<ValidationErrorDetailFCHAPI01> details) {
        super(message);
        this.details = details;
    }
}
src/main/java/com/example/conversation/history/fch01/exception/PermissionDeniedExceptionFCHAPI01.java