package com.example.service;

import com.example.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implements JWT generation and validation.
 */
@Service
public class TokenService implements ITokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.ms}")
    private long jwtExpiration;

    /**
     * Generates a JWT access token for a user.
     * @param user The user for whom to generate the token.
     * @return The JWT string.
     */
    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // Add any extra claims if needed, e.g., roles
        return buildToken(claims, user, jwtExpiration);
    }

    /**
     * Validates a token's integrity, expiration, and subject against UserDetails.
     * @param token The JWT string.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Extracts the user ID (subject) from the token's claims.
     * @param token The JWT string.
     * @return The user ID.
     */
    @Override
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}