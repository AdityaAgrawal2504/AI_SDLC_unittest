package com.example.auth.initiate.api_IA_9F3E.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the password service using Spring Security's PasswordEncoder.
 */
@Service
public class PasswordServiceImpl_IA_9F3E implements IPasswordService_IA_9F3E {

    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl_IA_9F3E(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verifies a plaintext password against its BCrypt hash.
     * @param plainTextPassword The raw password provided by the user.
     * @param hashedPassword The hashed password stored in the database.
     * @return True if the passwords match, false otherwise.
     */
    @Override
    public boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return passwordEncoder.matches(plainTextPassword, hashedPassword);
    }
}
```
```java