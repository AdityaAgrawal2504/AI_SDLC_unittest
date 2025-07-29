package com.example.service;

import com.example.model.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service interface for JWT (JSON Web Token) operations.
 */
public interface ITokenService {
    /**
     * Extracts the user ID from a JWT token.
     * @param token The JWT string.
     * @return The user ID (subject of the token).
     */
    String extractUserId(String token);

    /**
     * Generates an access token for a given user.
     * @param user The user for whom to generate the token.
     * @return The JWT string.
     */
    String generateAccessToken(User user);

    /**
     * Validates a JWT token against user details.
     * @param token The JWT string.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}