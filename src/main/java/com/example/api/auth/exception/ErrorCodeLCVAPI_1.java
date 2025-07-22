package com.example.api.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of standardized error codes for the API.
 * Maps machine-readable codes to HTTP statuses and human-readable messages.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCodeLCVAPI_1 {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "The request body is malformed or missing required fields."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "The phone number or password provided is incorrect."),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "ACCOUNT_LOCKED", "The user account is temporarily locked due to multiple failed login attempts."),
    ACCOUNT_INACTIVE(HttpStatus.FORBIDDEN, "ACCOUNT_INACTIVE", "The user account is inactive."),
    OTP_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "OTP_SERVICE_UNAVAILABLE", "The OTP service is currently unavailable."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected internal server error occurred.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
```

src/main/java/com/example/api/auth/exception/ApiExceptionLCVAPI_1.java
```java