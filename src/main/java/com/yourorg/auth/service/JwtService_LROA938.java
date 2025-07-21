package com.yourorg.auth.service;

/**
 * Interface for JWT (JSON Web Token) operations, specifically for creating OTP session tokens.
 */
public interface JwtService_LROA938 {

    /**
     * Generates a short-lived, single-use token for the OTP verification step.
     * @param phone The user's phone number to be used as the token subject.
     * @return A signed JWT string.
     */
    String generateOtpSessionToken(String phone);
}
```
```java
// src/main/java/com/yourorg/auth/service/impl/JwtServiceImpl_LROA938.java