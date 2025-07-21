package com.verifyotpapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * A dedicated service for hashing and verifying passwords/OTPs using Spring Security's PasswordEncoder.
 */
@Service
@RequiredArgsConstructor
public class PasswordHashingService_VOTP1 {

    private final PasswordEncoder passwordEncoder;

    /**
     * Hashes a raw OTP code.
     */
    public String hash(String rawOtp) {
        return passwordEncoder.encode(rawOtp);
    }

    /**
     * Matches a raw OTP code against a stored hash.
     */
    public boolean matches(String rawOtp, String hashedOtp) {
        return passwordEncoder.matches(rawOtp, hashedOtp);
    }
}
```
```java
// src/main/java/com/verifyotpapi/service/IAuthService_VOTP1.java