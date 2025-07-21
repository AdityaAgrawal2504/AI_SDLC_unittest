package com.yourorg.auth.service.impl;

import com.yourorg.auth.constants.ApiConstants_LROA938;
import com.yourorg.auth.dto.request.LoginRequest_LROA938;
import com.yourorg.auth.dto.response.LoginSuccessResponse_LROA938;
import com.yourorg.auth.enums.ErrorCode_LROA938;
import com.yourorg.auth.enums.UserStatus_LROA938;
import com.yourorg.auth.exception.ApplicationException_LROA938;
import com.yourorg.auth.model.User_LROA938;
import com.yourorg.auth.repository.UserRepository_LROA938;
import com.yourorg.auth.service.AuthenticationService_LROA938;
import com.yourorg.auth.service.JwtService_LROA938;
import com.yourorg.auth.service.OtpService_LROA938;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AuthenticationService, orchestrating the login and OTP flow.
 */
@Service
public class AuthenticationServiceImpl_LROA938 implements AuthenticationService_LROA938 {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl_LROA938.class);

    private final UserRepository_LROA938 userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService_LROA938 jwtService;
    private final OtpService_LROA938 otpService;

    public AuthenticationServiceImpl_LROA938(UserRepository_LROA938 userRepository,
                                            PasswordEncoder passwordEncoder,
                                            JwtService_LROA938 jwtService,
                                            OtpService_LROA938 otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }

    /**
     * Authenticates a user, checks account status, and dispatches an OTP.
     */
    @Override
    public LoginSuccessResponse_LROA938 loginAndRequestOtp(LoginRequest_LROA938 request) {
        long startTime = System.currentTimeMillis();
        logger.info("Login attempt started for phone: {}", request.getPhone());

        try {
            // 1. Find user by phone
            User_LROA938 user = userRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new ApplicationException_LROA938(ErrorCode_LROA938.USER_NOT_FOUND));

            // 2. Check for locked account
            if (user.getStatus() == UserStatus_LROA938.LOCKED) {
                throw new ApplicationException_LROA938(ErrorCode_LROA938.ACCOUNT_LOCKED);
            }

            // 3. Validate password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Here you might implement logic to increment a failed attempt counter
                throw new ApplicationException_LROA938(ErrorCode_LROA938.INVALID_CREDENTIALS);
            }

            // 4. Generate OTP session token
            String otpSessionToken = jwtService.generateOtpSessionToken(user.getPhone());

            // 5. Send OTP
            otpService.sendOtp(user.getPhone());

            // 6. Build and return success response
            return LoginSuccessResponse_LROA938.builder()
                    .status(ApiConstants_LROA938.SUCCESS_STATUS)
                    .message(ApiConstants_LROA938.OTP_SENT_SUCCESSFULLY)
                    .otpSessionToken(otpSessionToken)
                    .build();

        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("Login attempt finished for phone: {} in {}ms", request.getPhone(), (endTime - startTime));
        }
    }
}
```
```java
// src/main/java/com/yourorg/auth/controller/LoginController_LROA938.java