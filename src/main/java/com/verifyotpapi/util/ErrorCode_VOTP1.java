package com.verifyotpapi.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of machine-readable error codes and their corresponding HTTP statuses.
 */
@Getter
public enum ErrorCode_VOTP1 {
    VALIDATION_ERROR("VALIDATION_ERROR", "Invalid request body. Ensure all required fields are present and correctly formatted.", HttpStatus.BAD_REQUEST),
    INVALID_OTP("INVALID_OTP", "The OTP code provided is incorrect.", HttpStatus.UNAUTHORIZED),
    MAX_ATTEMPTS_REACHED("MAX_ATTEMPTS_REACHED", "Maximum verification attempts exceeded. Please request a new OTP.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "The verification token is invalid or has expired.", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "The verification token has expired. Please request a new OTP.", HttpStatus.NOT_FOUND),
    // This is a conceptual state. In this implementation, a used token is deleted, so it results in TOKEN_NOT_FOUND.
    // Kept for semantic completeness as per the spec.
    TOKEN_ALREADY_USED("TOKEN_ALREADY_USED", "This OTP has already been successfully used.", HttpStatus.NOT_FOUND),
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", "You have made too many requests. Please try again later.", HttpStatus.TOO_MANY_REQUESTS),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode_VOTP1(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
```
```java
// src/main/java/com/verifyotpapi/dto/request/VerifyOtpRequest_VOTP1.java