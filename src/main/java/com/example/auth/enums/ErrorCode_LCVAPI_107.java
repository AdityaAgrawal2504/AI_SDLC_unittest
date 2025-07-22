package com.example.auth.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of machine-readable error codes and their associated metadata.
 */
@Getter
public enum ErrorCode_LCVAPI_107 {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "The request is malformed or missing required fields."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "The phone number or password provided is incorrect."),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "Account is temporarily locked due to multiple failed login attempts."),
    ACCOUNT_INACTIVE(HttpStatus.FORBIDDEN, "The user account has been deactivated or not yet verified."),
    OTP_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Failed to send OTP. Please try again later."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred.");

    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode_LCVAPI_107(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }
}
src/main/java/com/example/auth/enums/UserStatus_LCVAPI_108.java