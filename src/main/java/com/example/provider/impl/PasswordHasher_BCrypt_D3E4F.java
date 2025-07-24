src/main/java/com/example/provider/impl/PasswordHasher_BCrypt_D3E4F.java
package com.example.provider.impl;

import com.example.provider.interfaces.IPasswordHasher_A1B2C;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementation of IPasswordHasher using BCrypt.
 */
@Component
public class PasswordHasher_BCrypt_D3E4F implements IPasswordHasher_A1B2C {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Hashes a raw password.
     * @param password The raw password.
     * @return The BCrypt-hashed password.
     */
    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Matches a raw password against an encoded hash.
     * @param rawPassword The raw password to check.
     * @param encodedPassword The stored hash.
     * @return true if the passwords match, false otherwise.
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}