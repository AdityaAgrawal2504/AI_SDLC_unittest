package com.example.service;

import com.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;
    private User user;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        // Use a valid Base64 encoded secret key for testing
        ReflectionTestUtils.setField(tokenService, "secretKey", "c2VjdXJpdHlzZWNyZXRrZXlmb3J0ZXN0aW5ncHVycG9zZXMxMjM0NTY3ODkw");
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", 86400000L); // 24 hours

        user = User.builder().id(UUID.randomUUID()).build();
    }

    @Test
    void generateAccessToken_shouldCreateValidToken() {
        String token = tokenService.generateAccessToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUserId_shouldReturnCorrectId() {
        String token = tokenService.generateAccessToken(user);
        String extractedId = tokenService.extractUserId(token);
        assertEquals(user.getId().toString(), extractedId);
    }

    @Test
    void isTokenValid_withValidToken_shouldReturnTrue() {
        String token = tokenService.generateAccessToken(user);
        assertTrue(tokenService.isTokenValid(token, user));
    }
}