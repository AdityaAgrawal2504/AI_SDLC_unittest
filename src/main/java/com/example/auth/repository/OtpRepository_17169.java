package com.example.auth.repository;

import com.example.auth.entity.OtpRecord_17169;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for managing OtpRecord entities.
 */
@Repository
public interface OtpRepository_17169 extends JpaRepository<OtpRecord_17169, UUID> {

    /**
     * Finds the most recent, unused OTP record for a given phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the active OtpRecord if found, otherwise empty.
     */
    Optional<OtpRecord_17169> findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(String phoneNumber);
}
src/main/java/com/example/auth/security/JwtTokenProvider_17169.java