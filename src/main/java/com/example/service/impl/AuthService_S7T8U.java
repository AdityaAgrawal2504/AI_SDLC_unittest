src/main/java/com/example/service/impl/AuthService_S7T8U.java
package com.example.service.impl;

import com.example.dto.LoginInitiateDto_G5H6I;
import com.example.dto.SessionDto_M9N0O;
import com.example.dto.SessionVerifyDto_J7K8L;
import com.example.exception.UnauthorizedException_Q7P8O;
import com.example.model.User_M1N2O;
import com.example.provider.interfaces.IJwtProvider_G5H6I;
import com.example.provider.interfaces.IPasswordHasher_A1B2C;
import com.example.repository.ISessionRepository_B9C8D;
import com.example.service.interfaces.IAuthService_P5Q6R;
import com.example.service.interfaces.IOtpService_J1K2L;
import com.example.service.interfaces.IUserService_D7E8F;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for authentication and session management.
 */
@Service
@RequiredArgsConstructor
public class AuthService_S7T8U implements IAuthService_P5Q6R {

    private final IUserService_D7E8F userService;
    private final IPasswordHasher_A1B2C passwordHasher;
    private final IOtpService_J1K2L otpService;
    private final IJwtProvider_G5H6I jwtProvider;
    private final ISessionRepository_B9C8D sessionRepository;
    
    /**
     * Initiates the login process by validating credentials and sending an OTP.
     * @param loginInitiateDto DTO containing login credentials.
     * @throws UnauthorizedException_Q7P8O if credentials are invalid.
     */
    @Override
    public void initiateLogin(LoginInitiateDto_G5H6I loginInitiateDto) {
        User_M1N2O user = userService.findByPhoneNumber(loginInitiateDto.getPhoneNumber());
        if (!passwordHasher.matches(loginInitiateDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException_Q7P8O("Invalid credentials.");
        }
        otpService.generateAndSendOtp(user);
    }

    /**
     * Verifies an OTP and creates a new session (JWT tokens).
     * @param sessionVerifyDto DTO containing phone number and OTP.
     * @return A DTO with access and refresh tokens.
     * @throws UnauthorizedException_Q7P8O if the OTP is invalid.
     */
    @Override
    public SessionDto_M9N0O verifyOtpAndCreateSession(SessionVerifyDto_J7K8L sessionVerifyDto) {
        User_M1N2O user = userService.findByPhoneNumber(sessionVerifyDto.getPhoneNumber());
        if (!otpService.verifyOtp(user, sessionVerifyDto.getOtp())) {
            throw new UnauthorizedException_Q7P8O("Invalid or expired OTP.");
        }
        // For simplicity, we are not storing the refresh token hash in this example.
        // In a real app, you would hash and save the refresh token to the Session table.
        return jwtProvider.generateTokens(user);
    }

    /**
     * Logs out the current user by invalidating their sessions.
     * @param currentUser The currently authenticated user.
     */
    @Override
    @Transactional
    public void logout(User_M1N2O currentUser) {
        // This is a simplified logout. A full implementation would involve
        // blacklisting the JWT or, if using refresh tokens, deleting the session
        // from the database.
        sessionRepository.deleteByUserId(currentUser.getId());
    }
}