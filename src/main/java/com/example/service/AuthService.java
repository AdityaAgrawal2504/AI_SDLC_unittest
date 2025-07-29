package com.example.service;

import com.example.dto.request.LoginInitiateRequest;
import com.example.dto.request.LoginVerifyRequest;
import com.example.dto.request.UserSignupRequest;
import com.example.dto.response.LoginInitiateResponse;
import com.example.dto.response.LoginVerifyResponse;
import com.example.dto.response.UserSignupResponse;
import com.example.model.User;
import com.example.repository.IUserRepository;
import com.example.service.exception.ConflictException;
import com.example.service.exception.ResourceNotFoundException;
import com.example.service.exception.UnauthorizedException;
import com.example.service.logging.IEventLogger;
import com.example.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implements authentication logic for user signup and login.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final IPasswordService passwordService;
    private final IOtpService otpService;
    private final ITokenService tokenService;
    private final IEventLogger eventLogger;

    /**
     * Creates a new user account if the phone number is not already taken.
     * @param request DTO with signup information.
     * @return DTO with created user details.
     */
    @Override
    @Transactional
    public UserSignupResponse signUp(UserSignupRequest request) {
        userRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(u -> {
            throw new ConflictException("User with this phone number already exists.");
        });

        User newUser = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .password(passwordService.hashPassword(request.getPassword()))
                .name("User " + request.getPhoneNumber().substring(request.getPhoneNumber().length() - 4)) // Default name
                .build();

        User savedUser = userRepository.save(newUser);
        eventLogger.log("UserCreated", "User signed up with ID: " + savedUser.getId());
        return DtoMapper.toUserSignupResponse(savedUser);
    }

    /**
     * Validates user credentials and sends an OTP for 2FA.
     * @param request DTO with login credentials.
     * @return DTO with the login session ID.
     */
    @Override
    @Transactional
    public LoginInitiateResponse initiateLogin(LoginInitiateRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User with this phone number not found."));

        if (!passwordService.comparePassword(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid password.");
        }

        UUID loginAttemptId = otpService.generateAndSendOtp(user);
        eventLogger.log("LoginInitiated", "Login initiated for user ID: " + user.getId());

        return new LoginInitiateResponse(loginAttemptId, "OTP sent to your phone number.");
    }

    /**
     * Verifies the OTP and generates a JWT access token upon success.
     * @param request DTO with login session ID and OTP.
     * @return DTO with the JWT access token.
     */
    @Override
    @Transactional
    public LoginVerifyResponse verifyLogin(LoginVerifyRequest request) {
        User user = otpService.verifyOtp(request.getLoginSessionId(), request.getOtp());

        String accessToken = tokenService.generateAccessToken(user);
        eventLogger.log("LoginSuccess", "Login successful for user ID: " + user.getId());

        return LoginVerifyResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}