package com.example.auth.service;

import com.example.auth.dto.LoginRequest_LCVAPI_104;
import com.example.auth.dto.LoginSuccessResponse_LCVAPI_105;
import com.example.auth.enums.ErrorCode_LCVAPI_107;
import com.example.auth.enums.UserStatus_LCVAPI_108;
import com.example.auth.exception.CustomApiException_LCVAPI_114;
import com.example.auth.model.User_LCVAPI_109;
import com.example.auth.repository.UserRepository_LCVAPI_110;
import com.example.auth.util.RequestIdUtil_LCVAPI_111;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implements the business logic for user authentication and OTP generation.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService_LCVAPI_118 implements IAuthenticationService_LCVAPI_117 {

    private final UserRepository_LCVAPI_110 userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService_LCVAPI_116 otpService;

    @Value("${app.security.max-failed-attempts:5}")
    private int maxFailedAttempts;

    /**
     * Validates user credentials, handles account status checks, and triggers OTP service.
     * @param request The login request DTO containing credentials.
     * @return A response DTO indicating successful OTP dispatch.
     */
    @Override
    @Transactional
    public LoginSuccessResponse_LCVAPI_105 loginWithCredentials(LoginRequest_LCVAPI_104 request) {
        long startTime = System.currentTimeMillis();
        log.info("Starting login process for phone number: {}", request.getPhoneNumber());

        User_LCVAPI_109 user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> {
                    log.warn("Login failed: Phone number {} not found.", request.getPhoneNumber());
                    return new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS);
                });

        validateUserStatus(user);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            handleFailedLoginAttempt(user);
            throw new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS);
        }

        resetFailedLoginAttempts(user);

        otpService.sendOtp(user.getPhoneNumber());

        log.info("Login credential verification successful for {}. OTP sent. Took {}ms", user.getPhoneNumber(), System.currentTimeMillis() - startTime);

        return new LoginSuccessResponse_LCVAPI_105(
                "OTP sent successfully to the registered phone number.",
                RequestIdUtil_LCVAPI_111.get()
        );
    }

    /**
     * Checks if the user's account is in a valid state for login.
     * @param user The user entity to validate.
     */
    private void validateUserStatus(User_LCVAPI_109 user) {
        if (user.getStatus() == UserStatus_LCVAPI_108.LOCKED) {
            log.warn("Login failed: Account for phone number {} is locked.", user.getPhoneNumber());
            throw new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.ACCOUNT_LOCKED);
        }
        if (user.getStatus() == UserStatus_LCVAPI_108.INACTIVE) {
            log.warn("Login failed: Account for phone number {} is inactive.", user.getPhoneNumber());
            throw new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.ACCOUNT_INACTIVE);
        }
    }

    /**
     * Increments the failed login attempt counter and locks the account if the threshold is reached.
     * @param user The user entity to update.
     */
    private void handleFailedLoginAttempt(User_LCVAPI_109 user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        log.warn("Invalid password for phone number {}. Failed attempt #{}.", user.getPhoneNumber(), user.getFailedLoginAttempts());

        if (user.getFailedLoginAttempts() >= maxFailedAttempts) {
            user.setStatus(UserStatus_LCVAPI_108.LOCKED);
            log.error("Account for phone number {} has been locked after {} failed attempts.", user.getPhoneNumber(), maxFailedAttempts);
        }
        userRepository.save(user);
    }

    /**
     * Resets the failed login attempt counter for a user upon successful login.
     * @param user The user entity to update.
     */
    private void resetFailedLoginAttempts(User_LCVAPI_109 user) {
        if (user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            log.info("Reset failed login attempts for phone number {}.", user.getPhoneNumber());
        }
    }
}
src/main/java/com/example/auth/service/IAuthenticationService_LCVAPI_117.java