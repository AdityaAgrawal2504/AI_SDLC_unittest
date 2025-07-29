package com.example.service.impl;

import com.example.service.IPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService implements IPasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Hashes a plaintext password.
     * @param password The plaintext password.
     * @return The hashed password string.
     */
    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Compares a plaintext password with its hashed version.
     * @param plainPassword The plaintext password.
     * @param hashedPassword The hashed password.
     * @return true if the passwords match, false otherwise.
     */
    @Override
    public boolean comparePassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}