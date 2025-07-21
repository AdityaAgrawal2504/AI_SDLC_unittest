package com.verifyotpapi.repository;

import com.verifyotpapi.model.OtpData_VOTP1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * Spring Data JPA repository for OtpData_VOTP1 entities.
 */
@Repository
public interface OtpDataRepository_VOTP1 extends JpaRepository<OtpData_VOTP1, Long> {

    /**
     * Finds an OTP record by its unique verification token.
     */
    Optional<OtpData_VOTP1> findByVerificationToken(String verificationToken);

    /**
     * Atomically increments the attempt counter for a given verification token.
     */
    @Modifying
    @Query("UPDATE OtpData_VOTP1 o SET o.attempts = o.attempts + 1 WHERE o.verificationToken = :token")
    int incrementAttempts(String token);

    /**
     * Deletes all OTP records that have expired.
     */
    void deleteByExpiresAtBefore(Instant now);
}
```
```java
// src/main/java/com/verifyotpapi/logging/StructuredLogger_VOTP1.java