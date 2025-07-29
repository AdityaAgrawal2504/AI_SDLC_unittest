package com.example.service.impl;

import com.example.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenServiceTest {

    private TokenService tokenService;
    private UserPrincipal userPrincipal;
    private final String jwtSecret = "your-very-long-and-secure-secret-key-that-is-at-least-256-bits-long";
    private final long jwtExpirationMs = 86400000; // 24 hours

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(jwtSecret, jwtExpirationMs);
        UUID userId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(userId, "+123456789", "password");
    }

    @Test
    void generateToken_and_validateToken_shouldSucceed() {
        String token = tokenService.generateToken(userPrincipal);
        
        assertThat(token).isNotNull();
        
        boolean isValid = tokenService.validateToken(token);
        assertThat(isValid).isTrue();
    }

    @Test
    void getUserIdFromToken_shouldReturnCorrectId() {
        String token = tokenService.generateToken(userPrincipal);
        UUID extractedUserId = tokenService.getUserIdFromToken(token);
        
        assertThat(extractedUserId).isEqualTo(userPrincipal.getId());
    }

    @Test
    void validateToken_withInvalidToken_shouldReturnFalse() {
        String invalidToken = "this.is.an.invalid.token";
        boolean isValid = tokenService.validateToken(invalidToken);
        assertThat(isValid).isFalse();
    }
}