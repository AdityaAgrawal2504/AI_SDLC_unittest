package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements IPasswordService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Hashes a raw password.
     * @param password The raw password string.
     * @return The encoded password hash.
     */
    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Verifies a raw password against an encoded hash.
     * @param rawPassword The raw password to check.
     * @param encodedPassword The stored password hash.
     * @return true if the passwords match, false otherwise.
     */
    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}