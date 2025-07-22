package com.example.usersearch.security;

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
 * Utility class for handling JWT creation, validation, and parsing.
 */
@Component
public class JwtUtil_A0B1 {

    private final SecretKey secretKey;
    private final long jwtExpirationMs;

    public JwtUtil_A0B1(@Value("${jwt.secret.key}") String secret,
                        @Value("${jwt.expiration.ms}") long jwtExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

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

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(AuthenticatedUser_A0B1 userDetails) {
        return Jwts.builder()
                .subject(userDetails.getId().toString())
                .claim("name", userDetails.getName())
                .claim("phone", userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    public Boolean validateToken(String token, AuthenticatedUser_A0B1 userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getId().toString()) && !isTokenExpired(token));
    }
}
```
src/main/java/com/example/usersearch/security/UserDetailsServiceImpl_A0B1.java
```java