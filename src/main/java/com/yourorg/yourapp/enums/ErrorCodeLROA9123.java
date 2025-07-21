package com.yourorg.yourapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of machine-readable error codes for the application.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCodeLROA9123 {

    VALIDATION_FAILED("VALIDATION_FAILED", "The request body is invalid or missing required fields.", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "The phone number or password provided is incorrect.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("USER_NOT_FOUND", "No account is associated with this phone number.", HttpStatus.NOT_FOUND),
    ACCOUNT_LOCKED("ACCOUNT_LOCKED", "This account is locked. Please try again later or contact support.", HttpStatus.FORBIDDEN),
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", "You have made too many login attempts. Please try again in a few minutes.", HttpStatus.TOO_MANY_REQUESTS),
    OTP_SERVICE_FAILURE("OTP_SERVICE_FAILURE", "The OTP service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An unexpected error occurred on the server.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;
}
```
src/main/java/com/yourorg/yourapp/dto/request/LoginRequestLROA9123.java
```java