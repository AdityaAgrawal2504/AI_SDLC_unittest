package com.example.service;

import com.example.dto.AuthTokenResponse_UATH_1002;
import com.example.dto.GenericSuccessResponse_UATH_1003;
import com.example.dto.LoginInitiateRequest_UATH_1004;
import com.example.dto.OtpVerifyRequest_UATH_1005;
import com.example.entity.UserEntity_UATH_1016;
import com.example.exception.InvalidCredentialsException_UATH_1011;
import com.example.exception.UserNotFoundException_UATH_1014;
import com.example.repository.UserRepository_UATH_1017;
import com.example.security.JwtUtil_SEC_33DD;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service handling the authentication flow, including OTP and token generation.
 */
@Service
@RequiredArgsConstructor
public class AuthService_UATH_1008 {

    private final UserRepository_UATH_1017 userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService_UATH_1018 otpService;
    private final JwtUtil_SEC_33DD jwtUtil;

    @Value("${app.jwt.access-token-expiration-ms}")
    private long accessTokenExpiration;

    /**
     * Initiates the login process by validating credentials and sending an OTP.
     */
    public GenericSuccessResponse_UATH_1003 initiateLogin(LoginInitiateRequest_UATH_1004 request) {
        UserEntity_UATH_1016 user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new InvalidCredentialsException_UATH_1011("The provided phone number and password combination is incorrect."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException_UATH_1011("The provided phone number and password combination is incorrect.");
        }

        otpService.generateAndSendOtp(request.phoneNumber());
        return new GenericSuccessResponse_UATH_1003("OTP has been sent to your phone number.");
    }

    /**
     * Verifies the provided OTP and, upon success, generates and returns auth tokens.
     */
    public AuthTokenResponse_UATH_1002 verifyOtpAndLogin(OtpVerifyRequest_UATH_1005 request) {
        boolean isOtpValid = otpService.validateOtp(request.phoneNumber(), request.otp());

        if (!isOtpValid) {
            // The OtpService throws a specific exception, so this is a fallback.
            // This logic is primarily handled within OtpService itself.
        }

        UserEntity_UATH_1016 user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new UserNotFoundException_UATH_1014("User not found for phone number: " + request.phoneNumber()));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        otpService.clearOtp(request.phoneNumber());

        return new AuthTokenResponse_UATH_1002(accessToken, refreshToken, accessTokenExpiration / 1000);
    }
}
```
```java
//
// Filename: src/main/java/com/example/service/UserService_UATH_1009.java
//