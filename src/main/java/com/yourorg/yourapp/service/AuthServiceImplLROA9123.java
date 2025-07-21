package com.yourorg.yourapp.service;

import com.yourorg.yourapp.dto.request.LoginRequestLROA9123;
import com.yourorg.yourapp.dto.response.LoginSuccessResponseLROA9123;
import com.yourorg.yourapp.enums.ErrorCodeLROA9123;
import com.yourorg.yourapp.exception.ApiExceptionLROA9123;
import com.yourorg.yourapp.model.UserLROA9123;
import com.yourorg.yourapp.repository.UserRepositoryLROA9123;
import com.yourorg.yourapp.util.JwtUtilLROA9123;
import com.yourorg.yourapp.util.OtpGeneratorUtilLROA9123;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements the authentication business logic.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImplLROA9123 implements AuthServiceLROA9123 {

    private final UserRepositoryLROA9123 userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpGeneratorUtilLROA9123 otpGenerator;
    private final OtpServiceClientLROA9123 otpServiceClient;
    private final JwtUtilLROA9123 jwtUtil;

    /**
     * {@inheritDoc}
     *
     * mermaid
     * sequenceDiagram
     *     participant C as Client
     *     participant S as AuthService
     *     participant DB as Database
     *     participant OTP as OtpService
     *     participant JWT as JwtUtil
     *
     *     C->>S: loginAndRequestOtp(phone, password)
     *     S->>DB: findByPhone(phone)
     *     alt User Not Found
     *         DB-->>S: Empty
     *         S-->>C: 404 USER_NOT_FOUND
     *     else User Found
     *         DB-->>S: User record
     *         S->>S: Check if account is locked
     *         alt Account Locked
     *             S-->>C: 403 ACCOUNT_LOCKED
     *         end
     *         S->>S: Verify password(raw, hashed)
     *         alt Invalid Password
     *             S-->>C: 401 INVALID_CREDENTIALS
     *         end
     *         S->>S: generateOtp()
     *         S->>OTP: sendOtp(phone, otp)
     *         OTP-->>S: Success
     *         S->>JWT: generateOtpSessionToken(phone)
     *         JWT-->>S: otpSessionToken
     *         S-->>C: 200 OK with LoginSuccessResponse
     *     end
     */
    @Override
    @Transactional
    public LoginSuccessResponseLROA9123 loginAndRequestOtp(LoginRequestLROA9123 request) {
        log.info("Attempting login for phone: {}", request.getPhone());
        
        // 1. Find user by phone number
        UserLROA9123 user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found for phone {}", request.getPhone());
                    return new ApiExceptionLROA9123(ErrorCodeLROA9123.USER_NOT_FOUND);
                });

        // 2. Check for locked account
        if (user.isAccountLocked()) {
            log.warn("Login failed: Account is locked for phone {}", request.getPhone());
            throw new ApiExceptionLROA9123(ErrorCodeLROA9123.ACCOUNT_LOCKED);
        }

        // 3. Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Here you might implement logic to increment a failed attempt counter
            log.warn("Login failed: Invalid password for phone {}", request.getPhone());
            throw new ApiExceptionLROA9123(ErrorCodeLROA9123.INVALID_CREDENTIALS);
        }

        // 4. Generate OTP
        String otp = otpGenerator.generateOtp();

        // 5. Send OTP via external service
        // The service client will throw an exception on failure
        otpServiceClient.sendOtp(user.getPhone(), otp);
        
        // Here you would typically save the OTP hash and its expiry to the database or Redis
        // associated with the user, to verify it in the next step.
        log.info("Successfully sent OTP to phone {}", user.getPhone());

        // 6. Generate short-lived session token for the OTP verification step
        String otpSessionToken = jwtUtil.generateOtpSessionToken(user.getPhone());

        return LoginSuccessResponseLROA9123.builder()
                .status("success")
                .message("OTP has been sent successfully. Please check your device.")
                .otpSessionToken(otpSessionToken)
                .build();
    }
}
```
src/main/java/com/yourorg/yourapp/controller/AuthControllerV1_LROA9123.java
```java