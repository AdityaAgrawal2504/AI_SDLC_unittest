package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when a registration attempt is made for an existing phone number.
 * Maps to HTTP 409 Conflict.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException_F4B8 extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with a detail message.
     * @param message The detail message.
     */
    public UserAlreadyExistsException_F4B8(String message) {
        super(message);
    }
}
```
```java
src/main/java/com/example/exception/PasswordHashingException_F4B8.java