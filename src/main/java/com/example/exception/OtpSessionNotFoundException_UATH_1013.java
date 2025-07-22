package com.example.exception;

/**
 * Exception thrown when no active OTP session is found for a phone number.
 */
public class OtpSessionNotFoundException_UATH_1013 extends RuntimeException {
    public OtpSessionNotFoundException_UATH_1013(String message) {
        super(message);
    }
}
```
```java
//
// Filename: src/main/java/com/example/exception/UserNotFoundException_UATH_1014.java
//