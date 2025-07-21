package com.verifyotpapi.service;

import com.verifyotpapi.config.AppProperties_VOTP1;
import com.verifyotpapi.dto.response.VerifyOtpSuccessResponse_VOTP1;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service responsible for creating JWT access and refresh tokens.
 */
@Service
public class TokenService_VOTP1 implements ITokenService_VOTP1 {

    private final SecretKey jwtSecretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;
    private final AppProperties_VOTP1 appProperties;

    public TokenService_VOTP1(AppProperties_VOTP1 appProperties) {
        this.appProperties = appProperties;
        this.jwtSecretKey = Keys.hmacShaKeyFor(appProperties.jwt().secret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = appProperties.jwt().accessTokenExpirationSeconds() * 1000;
        this.refreshTokenExpirationMs = appProperties.jwt().refreshTokenExpirationDays() * 24 * 60 * 60 * 1000;
    }

    /**
     * Generates a complete session token response, including access and refresh tokens.
     */
    @Override
    public VerifyOtpSuccessResponse_VOTP1 generateSessionTokens(String userId) {
        String accessToken = generateToken(userId, accessTokenExpirationMs, new HashMap<>());
        String refreshToken = generateToken(userId, refreshTokenExpirationMs, new HashMap<>());

        return VerifyOtpSuccessResponse_VOTP1.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(appProperties.jwt().accessTokenExpirationSeconds())
            .tokenType("Bearer")
            .build();
    }

    /**
     * Creates a JWT token for a given user with a specified expiration and claims.
     */
    private String generateToken(String subject, long expirationMs, Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(now)
            .expiration(expiryDate)
            .id(UUID.randomUUID().toString())
            .signWith(jwtSecretKey)
            .compact();
    }
}
```
```java
// src/main/java/com/verifyotpapi/service/PasswordHashingService_VOTP1.java