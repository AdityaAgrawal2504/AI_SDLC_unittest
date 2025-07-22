package com.example.auth.config;

import com.example.auth.enums.UserStatus_LCVAPI_108;
import com.example.auth.model.User_LCVAPI_109;
import com.example.auth.repository.UserRepository_LCVAPI_110;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Populates the database with initial sample data upon application startup.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer_LCVAPI_120 implements CommandLineRunner {

    private final UserRepository_LCVAPI_110 userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Runs on startup to create sample users if they don't exist.
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Executing data initializer...");
        // Active User
        createUserIfNotFound("+14155552671", "MyS3cur3P@ssw0rd!", UserStatus_LCVAPI_108.ACTIVE);
        // Inactive User
        createUserIfNotFound("+15005550001", "InactiveP@ss!", UserStatus_LCVAPI_108.INACTIVE);
        // Locked User (created as active, can be locked by testing failed attempts)
        createUserIfNotFound("+15005550002", "ToBeLocked!", UserStatus_LCVAPI_108.ACTIVE);
        log.info("Data initialization complete.");
    }

    /**
     * Helper method to create a user only if one with the given phone number doesn't already exist.
     */
    private void createUserIfNotFound(String phoneNumber, String password, UserStatus_LCVAPI_108 status) {
        if (userRepository.findByPhoneNumber(phoneNumber).isEmpty()) {
            User_LCVAPI_109 user = new User_LCVAPI_109();
            user.setPhoneNumber(phoneNumber);
            user.setPassword(passwordEncoder.encode(password));
            user.setStatus(status);
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            log.info("Created sample user with phone number: {} and status: {}", phoneNumber, status);
        } else {
            log.info("User with phone number {} already exists, skipping creation.", phoneNumber);
        }
    }
}
src/main/java/com/example/auth/config/RequestIdFilter_LCVAPI_112.java