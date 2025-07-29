package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implements password hashing and comparison using Spring Security's PasswordEncoder.
 */
@Service
@RequiredArgsConstructor
public class PasswordService implements IPasswordService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Hashes a password using the configured PasswordEncoder.
     * @param rawPassword The plain-text password.
     * @return The hashed password string.
     */
    @Override
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Compares a raw password with an encoded one.
     * @param rawPassword The plain-text password.
     * @param encodedPassword The hashed password from storage.
     * @return True if the passwords match.
     */
    @Override
    public boolean comparePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}