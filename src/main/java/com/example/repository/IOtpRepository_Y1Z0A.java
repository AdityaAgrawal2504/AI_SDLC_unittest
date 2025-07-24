src/main/java/com/example/repository/IOtpRepository_Y1Z0A.java

<ctrl60>
<ctrl62>
<ctrl61>
package com.example.repository;

import com.example.model.Otp_Y9Z0A;
import com.example.model.User_M1N2O;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOtpRepository_Y1Z0A extends JpaRepository<Otp_Y9Z0A, UUID> {
    Optional<Otp_Y9Z0A> findByUserAndCodeAndExpiresAtAfter(User_M1N2O user, String code, LocalDateTime now);
}