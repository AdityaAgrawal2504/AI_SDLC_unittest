package com.example.exception;

/**
 * Exception thrown for incorrect login credentials (phone/password).
 */
public class InvalidCredentialsException_UATH_1011 extends RuntimeException {
    public InvalidCredentialsException_UATH_1011(String message) {
        super(message);
    }
}
```
```java
//
// Filename: src/main/java/com/example/exception/OtpValidationException_UATH_1012.java
//