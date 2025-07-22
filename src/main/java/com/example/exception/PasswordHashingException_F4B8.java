package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when there is a failure during the password hashing process.
 * Maps to HTTP 500 Internal Server Error.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PasswordHashingException_F4B8 extends RuntimeException {

    /**
     * Constructs a new PasswordHashingException with a detail message and cause.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public PasswordHashingException_F4B8(String message, Throwable cause) {
        super(message, cause);
    }
}
```
```java
src/main/java/com/example/exception/GlobalExceptionHandler_F4B8.java