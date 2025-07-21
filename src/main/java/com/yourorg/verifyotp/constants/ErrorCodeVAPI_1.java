package com.yourorg.verifyotp.constants;

import org.springframework.http.HttpStatus;

public enum ErrorCodeVAPI_1 {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Invalid request body. Ensure all required fields are present and correctly formatted."),
    INVALID_OTP(HttpStatus.UNAUTHORIZED, "The OTP code provided is incorrect."),
    MAX_ATTEMPTS_REACHED(HttpStatus.UNAUTHORIZED, "Maximum verification attempts exceeded. Please request a new OTP."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "The verification token is invalid or has expired."),
    TOKEN_EXPIRED(HttpStatus.NOT_FOUND, "The verification token has expired. Please request a new OTP."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");

    private final HttpStatus status;
    private final String errorMessage;

    ErrorCodeVAPI_1(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
```
```java
// Repository: OtpRepositoryVAPI_1.java