package com.example.security;

import com.example.entity.UserEntity_UATH_1016;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for handling JWT-related operations like generation and validation.
 */
@Component
public class JwtUtil_SEC_33DD {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil_SEC_33DD(@Value("${app.jwt.secret}") String secret,
                            @Value("${app.jwt.access-token-expiration-ms}") long accessTokenExpiration,
                            @Value("${app.jwt.refresh-token-expiration-ms}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Extracts the user ID (subject) from the token.
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates an access token for a user.
     */
    public String generateAccessToken(UserEntity_UATH_1016 user) {
        return buildToken(new HashMap<>(), user, accessTokenExpiration);
    }

    /**
     * Generates a refresh token for a user.
     */
    public String generateRefreshToken(UserEntity_UATH_1016 user) {
        return buildToken(new HashMap<>(), user, refreshTokenExpiration);
    }

    /**
     * Validates if the token is correct and not expired.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserEntity_UATH_1016 user, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
```
```java
//
// Filename: src/main/java/com/example/security/UserDetailsServiceImpl_SEC_33EE.java
//