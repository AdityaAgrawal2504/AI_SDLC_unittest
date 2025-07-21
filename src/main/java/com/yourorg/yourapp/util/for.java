package com.yourorg.yourapp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility class for generating JSON Web Tokens (JWTs).
 */
@Component
public class JwtUtilLROA9123 {

    private final SecretKey secretKey;
    private final long otpExpirationMs;

    public JwtUtilLROA9123(@Value("${app.security.jwt.otp-secret}") String secret,
                          @Value("${app.security.jwt.otp-expiration-ms}") long otpExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.otpExpirationMs = otpExpirationMs;
    }

    /**
     * Generates a short-lived token specifically for the OTP verification step.
     * @param phone The user's phone number to embed as the subject.
     * @return A JWT string.
     */
    public String generateOtpSessionToken(String phone) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + otpExpirationMs);

        return Jwts.builder()
                .setSubject(phone)
                .claim("scope", "otp_verify")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
}
```
src/main/java/com/yourorg/yourapp/util/OtpGeneratorUtilLROA9123.java
```java