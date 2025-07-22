package com.example.api.auth.service;

import com.example.api.auth.domain.UserLCVAPI_1;
import com.example.api.auth.domain.UserStatusLCVAPI_1;
import com.example.api.auth.dto.LoginRequestLCVAPI_1;
import com.example.api.auth.dto.LoginSuccessResponseLCVAPI_1;
import com.example.api.auth.exception.AccountInactiveExceptionLCVAPI_1;
import com.example.api.auth.exception.AccountLockedExceptionLCVAPI_1;
import com.example.api.auth.exception.InvalidCredentialsExceptionLCVAPI_1;
import com.example.api.auth.repository.UserRepositoryLCVAPI_1;
import com.example.util.RequestIdUtilLCVAPI_1;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the authentication service logic.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceLCVAPI_1 implements IAuthenticationServiceLCVAPI_1 {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceLCVAPI_1.class);
    
    private final UserRepositoryLCVAPI_1 userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpServiceLCVAPI_1 otpService;

    @Value("${app.security.max-failed-attempts:5}")
    private int maxFailedAttempts;

    /**
     * Verifies credentials, checks account status, and triggers OTP dispatch.
     * @param request The login DTO.
     * @return A success response if validation passes and OTP is sent.
     */
    @Override
    @Transactional
    public LoginSuccessResponseLCVAPI_1 loginWithCredentials(LoginRequestLCVAPI_1 request) {
        long startTime = System.currentTimeMillis();
        logger.info("Login process started for phone number: {}", request.getPhoneNumber());

        UserLCVAPI_1 user = userRepository.findByPhoneNumber(request.getPhoneNumber())
            .orElseThrow(InvalidCredentialsExceptionLCVAPI_1::new);

        validateUserStatus(user);

        if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            handleSuccessfulLogin(user);
            otpService.sendOtp(user.getPhoneNumber());
            logger.info("Login successful and OTP sent for phone number: {}", user.getPhoneNumber());
            
            long endTime = System.currentTimeMillis();
            logger.info("loginWithCredentials execution time: {} ms", (endTime - startTime));
            
            return new LoginSuccessResponseLCVAPI_1(
                "OTP sent successfully to the registered phone number.",
                RequestIdUtilLCVAPI_1.getRequestId()
            );
        } else {
            handleFailedLogin(user);
            logger.warn("Invalid password attempt for phone number: {}", user.getPhoneNumber());
            throw new InvalidCredentialsExceptionLCVAPI_1();
        }
    }

    /**
     * Checks if the user's account is active.
     * @param user The user entity to check.
     * @throws AccountLockedExceptionLCVAPI_1 if the account is locked.
     * @throws AccountInactiveExceptionLCVAPI_1 if the account is inactive.
     */
    private void validateUserStatus(UserLCVAPI_1 user) {
        if (user.getStatus() == UserStatusLCVAPI_1.LOCKED) {
            throw new AccountLockedExceptionLCVAPI_1();
        }
        if (user.getStatus() == UserStatusLCVAPI_1.INACTIVE) {
            throw new AccountInactiveExceptionLCVAPI_1();
        }
    }

    /**
     * Resets the failed login attempt counter for a user upon successful login.
     * @param user The user entity to update.
     */
    private void handleSuccessfulLogin(UserLCVAPI_1 user) {
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    /**
     * Increments the failed login attempt counter and locks the account if the threshold is met.
     * @param user The user entity to update.
     */
    private void handleFailedLogin(UserLCVAPI_1 user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        if (user.getFailedLoginAttempts() >= maxFailedAttempts) {
            user.setStatus(UserStatusLCVAPI_1.LOCKED);
            logger.warn("Account for phone number {} has been locked due to too many failed login attempts.", user.getPhoneNumber());
        }
        userRepository.save(user);
    }
}
```

src/main/java/com/example/api/auth/service/OtpServiceLCVAPI_1.java
```java