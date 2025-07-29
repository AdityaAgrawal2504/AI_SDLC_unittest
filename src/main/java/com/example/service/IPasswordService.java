package com.example.service;

/**
 * Service interface for password hashing and comparison.
 */
public interface IPasswordService {
    /**
     * Hashes a raw password.
     * @param rawPassword The plain-text password.
     * @return The hashed password string.
     */
    String hashPassword(String rawPassword);

    /**
     * Compares a raw password with a hashed password.
     * @param rawPassword The plain-text password.
     * @param encodedPassword The hashed password from storage.
     * @return True if the passwords match, false otherwise.
     */
    boolean comparePassword(String rawPassword, String encodedPassword);
}