package com.yourorg.userregistration.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implements password hashing using Spring Security's PasswordEncoder.
 */
@Component
public class PasswordHasherURAPI_1201 implements IPasswordHasherURAPI_1201 {

    private final PasswordEncoder passwordEncoder;

    public PasswordHasherURAPI_1201(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Hashes a plain-text password using the configured PasswordEncoder.
     * @param plainTextPassword The password to hash.
     * @return The resulting hash.
     */
    @Override
    public String hash(String plainTextPassword) {
        return passwordEncoder.encode(plainTextPassword);
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/config/SecurityConfigURAPI_1201.java