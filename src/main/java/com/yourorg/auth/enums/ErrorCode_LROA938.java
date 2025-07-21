package com.yourorg.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of machine-readable error codes and their associated messages and HTTP statuses.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode_LROA938 {
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "The request body is invalid or missing required fields."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "The phone number or password provided is incorrect."),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "ACCOUNT_LOCKED", "This account is locked. Please try again later or contact support."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "No account is associated with this phone number."),
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMIT_EXCEEDED", "You have made too many login attempts. Please try again in a few minutes."),
    OTP_SERVICE_FAILURE(HttpStatus.SERVICE_UNAVAILABLE, "OTP_SERVICE_FAILURE", "The OTP service is temporarily unavailable. Please try again later."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected error occurred on the server.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
```
```java
// src/main/java/com/yourorg/auth/enums/UserStatus_LROA938.java