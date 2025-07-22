package com.example.exception;

/**
 * Exception thrown when attempting to register a user with a phone number that already exists.
 */
public class UserAlreadyExistsException_UATH_1010 extends RuntimeException {
    public UserAlreadyExistsException_UATH_1010(String message) {
        super(message);
    }
}
```
```java
//
// Filename: src/main/java/com/example/exception/InvalidCredentialsException_UATH_1011.java
//