package com.example.auth.initiate.api_IA_9F3E.exception;

import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not found in the system.
 */
public class UserNotFoundException_IA_9F3E extends BaseApiException_IA_9F3E {

    private static final String DEFAULT_MESSAGE = "No user found with the provided phone number.";

    /**
     * Constructs a UserNotFoundException with a default message.
     */
    public UserNotFoundException_IA_9F3E() {
        super(DEFAULT_MESSAGE, HttpStatus.NOT_FOUND, ErrorCode_IA_9F3E.USER_NOT_FOUND);
    }
}
```
```java