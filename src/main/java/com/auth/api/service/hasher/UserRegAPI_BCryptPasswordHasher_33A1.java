package com.auth.api.service.hasher;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implements the password hasher using Spring Security's BCryptPasswordEncoder.
 */
@Service
@RequiredArgsConstructor
public class UserRegAPI_BCryptPasswordHasher_33A1 implements UserRegAPI_PasswordHasher_33A1 {

    private final PasswordEncoder passwordEncoder;

    /**
     * Hashes a plain text password using BCrypt.
     * @param plainTextPassword The password to hash.
     * @return The BCrypt hash.
     */
    @Override
    public String hash(String plainTextPassword) {
        return passwordEncoder.encode(plainTextPassword);
    }
}
```
```java
// src/main/java/com/auth/api/logging/UserRegAPI_Loggable_33A1.java