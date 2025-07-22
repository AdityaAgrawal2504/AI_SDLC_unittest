package com.example.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * JPA Entity representing an OTP record in the database.
 */
@Entity
@Data
@NoArgsConstructor
public class OtpRecord_17169 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String phoneNumber;
    private String otpCode;
    private ZonedDateTime createdAt;
    private ZonedDateTime expiresAt;
    private int attempts;
    private boolean used;

    // A real application would link to a proper User entity
    private String userId;

    public OtpRecord_17169(String phoneNumber, String otpCode, ZonedDateTime expiresAt, String userId) {
        this.phoneNumber = phoneNumber;
        this.otpCode = otpCode;
        this.createdAt = ZonedDateTime.now();
        this.expiresAt = expiresAt;
        this.userId = userId;
        this.attempts = 0;
        this.used = false;
    }
}
src/main/java/com/example/auth/repository/OtpRepository_17169.java