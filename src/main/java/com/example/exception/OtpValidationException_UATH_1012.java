package com.example.exception;

/**
 * Exception thrown when OTP validation fails (e.g., incorrect, expired).
 */
public class OtpValidationException_UATH_1012 extends RuntimeException {
    public OtpValidationException_UATH_1012(String message) {
        super(message);
    }
}
```
```java
//
// Filename: src/main/java/com/example/exception/OtpSessionNotFoundException_UATH_1013.java
//