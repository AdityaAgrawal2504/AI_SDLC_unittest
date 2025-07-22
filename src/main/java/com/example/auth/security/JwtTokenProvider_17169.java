package com.example.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Handles the creation of JWT session tokens.
 */
@Component
public class JwtTokenProvider_17169 {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.expiration-seconds}")
    private long jwtExpirationInSeconds;

    private Key secretKey;

    /**
     * Initializes the component by creating a secure key from the configured secret.
     */
    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a new JWT for a given user.
     * @param userId The unique identifier of the user.
     * @param phoneNumber The phone number associated with the user session.
     * @return The generated JWT string.
     */
    public String createToken(String userId, String phoneNumber) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationInSeconds * 1000);

        return Jwts.builder()
                .setClaims(Map.of("phoneNumber", phoneNumber))
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
src/main/java/com/example/auth/logging/StructuredLogger_AuthVerifyOtp_17169.java