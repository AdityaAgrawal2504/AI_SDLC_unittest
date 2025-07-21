package com.yourorg.auth.service.impl;

import com.yourorg.auth.constants.ApiConstants_LROA938;
import com.yourorg.auth.service.JwtService_LROA938;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of JwtService using the jjwt library.
 */
@Service
public class JwtServiceImpl_LROA938 implements JwtService_LROA938 {

    private final SecretKey secretKey;
    private final long otpSessionExpirationMs;

    public JwtServiceImpl_LROA938(
            @Value("${jwt.secret.key}") String secret,
            @Value("${jwt.otp.session.expiration.ms}") long otpSessionExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.otpSessionExpirationMs = otpSessionExpirationMs;
    }

    /**
     * Generates a short-lived, single-use token for the OTP verification step.
     */
    @Override
    public String generateOtpSessionToken(String phone) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", ApiConstants_LROA938.OTP_VERIFY_SCOPE);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + otpSessionExpirationMs);

        return Jwts.builder()
                .claims(claims)
                .subject(phone)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
}
```
```java
// src/main/java/com/yourorg/auth/service/OtpService_LROA938.java