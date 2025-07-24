package com.example.messagingapp.service;

import com.example.messagingapp.model.User;
import io.jsonwebtoken.Claims;

public interface TokenService {
    /**
     * Generates a JWT access token for a given user.
     * @param user The user entity for whom the token is generated.
     * @return The JWT as a string.
     */
    String generateAccessToken(User user);

    /**
     * Verifies a JWT and extracts its claims.
     * @param token The JWT string to verify.
     * @return The claims contained within the token.
     */
    Claims verifyToken(String token);
}