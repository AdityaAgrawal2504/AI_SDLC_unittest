package com.verifyotpapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * JPA Entity representing an OTP record in the data store.
 */
@Entity
@Table(name = "otp_data", uniqueConstraints = {
    @UniqueConstraint(columnNames = "verificationToken")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpData_VOTP1 {

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
}
```
```java
// src/main/java/com/verifyotpapi/repository/OtpDataRepository_VOTP1.java