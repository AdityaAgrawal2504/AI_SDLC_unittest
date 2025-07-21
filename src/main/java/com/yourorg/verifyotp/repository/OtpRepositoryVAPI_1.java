package com.yourorg.verifyotp.repository;

import com.yourorg.verifyotp.model.OtpDataVAPI_1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepositoryVAPI_1 extends JpaRepository<OtpDataVAPI_1, Long> {

    /**
     * Finds an OTP record by its unique verification token.
     */
    Optional<OtpDataVAPI_1> findByVerificationToken(String verificationToken);

    /**
     * Deletes an OTP record by its unique verification token.
     */
    void deleteByVerificationToken(String verificationToken);
}
```
```java
// Config: SecurityConfigVAPI_1.java