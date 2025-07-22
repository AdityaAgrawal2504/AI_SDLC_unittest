package com.example.auth.initiate.api_IA_9F3E.exception;

import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown for invalid user credentials (phone number or password).
 */
public class InvalidCredentialsException_IA_9F3E extends BaseApiException_IA_9F3E {

    private static final String DEFAULT_MESSAGE = "Invalid phone number or password.";

    /**
     * Constructs an InvalidCredentialsException with a default message.
     */
    public InvalidCredentialsException_IA_9F3E() {
        super(DEFAULT_MESSAGE, HttpStatus.UNAUTHORIZED, ErrorCode_IA_9F3E.INVALID_CREDENTIALS);
    }
}
```
```java