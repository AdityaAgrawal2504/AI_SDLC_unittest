package com.example.conversation.history.fch01.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of machine-readable error codes.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCodeFCHAPI01 {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "One or more parameters are invalid."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Authentication credentials were not provided or are invalid."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "You do not have permission to access this resource."),
    CONVERSATION_NOT_FOUND(HttpStatus.NOT_FOUND, "The specified conversation could not be found."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred. Please try again later.");

    private final HttpStatus status;
    private final String defaultMessage;
}
src/main/java/com/example/conversation/history/fch01/enums/MessageTypeFCHAPI01.java