package com.yourorg.yourapp.config;

import com.yourorg.yourapp.model.UserLROA9123;
import com.yourorg.yourapp.repository.UserRepositoryLROA9123;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes the database with sample data for demonstration purposes.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializerLROA9123 implements CommandLineRunner {

    private final UserRepositoryLROA9123 userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Checking for initial user data...");
        if (userRepository.count() == 0) {
            log.info("No users found. Creating sample users.");

            // 1. A valid, active user
            userRepository.save(UserLROA9123.builder()
                .phone("+14155552671")
                .password(passwordEncoder.encode("MyS3cur3P@ssw0rd!"))
                .accountLocked(false)
                .enabled(true)
                .build());

            // 2. A user with a locked account
            userRepository.save(UserLROA9123.builder()
                .phone("+15005550001")
                .password(passwordEncoder.encode("LockedOutPassword123"))
                .accountLocked(true)
                .enabled(true)
                .build());

            // 3. A user to test OTP service failure simulation
             userRepository.save(UserLROA9123.builder()
                .phone("+15005550000")
                .password(passwordEncoder.encode("OtpFailPassword123"))
                .accountLocked(false)
                .enabled(true)
                .build());

            log.info("Created {} sample users.", userRepository.count());
        } else {
            log.info("Database already contains data. Skipping initialization.");
        }
    }
}
```
src/test/java/com/yourorg/yourapp/service/AuthServiceImplLROA9123Test.java
```java