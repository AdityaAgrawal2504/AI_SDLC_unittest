package com.fetchmessagesapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

/**
 * Utility for handling JSON Web Tokens (JWT).
 */
@Component
public class JwtUtilFMA1 {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtilFMA1(@Value("${fetchmessages.api.jwt.secret}") String secret,
                       @Value("${fetchmessages.api.jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Extracts the user ID (subject) from a JWT token.
     * @param token The JWT token.
     * @return The user ID as a UUID.
     */
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    /**
     * Extracts the expiration date from a JWT token.
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token.
     * @param token The JWT token.
     * @param claimsResolver A function to resolve the claim.
     * @return The claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates a JWT token.
     * @param token The JWT token.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/service/MessageMapperFMA1.java