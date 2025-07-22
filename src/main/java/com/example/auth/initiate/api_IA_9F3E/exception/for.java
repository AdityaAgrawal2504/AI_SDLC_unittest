package com.example.auth.initiate.api_IA_9F3E.exception;

import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Abstract base class for custom API exceptions.
 */
@Getter
public abstract class BaseApiException_IA_9F3E extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorCode_IA_9F3E errorCode;

    /**
     * Constructs a new base API exception.
     * @param message The detail message.
     * @param httpStatus The HTTP status to be returned.
     * @param errorCode The application-specific error code.
     */
    public BaseApiException_IA_9F3E(String message, HttpStatus httpStatus, ErrorCode_IA_9F3E errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
```
```java