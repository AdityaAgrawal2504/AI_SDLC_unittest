package com.yourorg.verifyotp.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "otp_data")
public class OtpDataVAPI_1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String hashedOtp;

    @Column(nullable = false, unique = true)
    private String verificationToken;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private int attempts;

    // Constructors
    public OtpDataVAPI_1() {}

    public OtpDataVAPI_1(String userId, String hashedOtp, String verificationToken, Instant expiresAt) {
        this.userId = userId;
        this.hashedOtp = hashedOtp;
        this.verificationToken = verificationToken;
        this.expiresAt = expiresAt;
        this.attempts = 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHashedOtp() {
        return hashedOtp;
    }

    public void setHashedOtp(String hashedOtp) {
        this.hashedOtp = hashedOtp;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
```
```java
// Exception: CustomApiExceptionVAPI_1.java