package com.example.service.impl;

import com.example.dto.LoginInitiateDto;
import com.example.dto.LoginVerifyDto;
import com.example.dto.UserSignupDto;
import com.example.exception.ApiException;
import com.example.exception.ResourceConflictException;
import com.example.model.LoginAttempt;
import com.example.model.User;
import com.example.repository.ILoginAttemptRepository;
import com.example.repository.IUserRepository;
import com.example.security.UserPrincipal;
import com.example.service.IAuthService;
import com.example.service.IOtpService;
import com.example.service.IPasswordService;
import com.example.service.ITokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final ILoginAttemptRepository loginAttemptRepository;
    private final IPasswordService passwordService;
    private final IOtpService otpService;
    private final ITokenService tokenService;
    private final int maxOtpAttempts;
    private final long otpValidityMinutes;

    public AuthService(IUserRepository userRepository, ILoginAttemptRepository loginAttemptRepository,
                       IPasswordService passwordService, IOtpService otpService, ITokenService tokenService,
                       @Value("${otp.max.attempts:3}") int maxOtpAttempts,
                       @Value("${otp.validity.duration.minutes:5}") long otpValidityMinutes) {
        this.userRepository = userRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.passwordService = passwordService;
        this.otpService = otpService;
        this.tokenService = tokenService;
        this.maxOtpAttempts = maxOtpAttempts;
        this.otpValidityMinutes = otpValidityMinutes;
    }

    /**
     * Registers a new user.
     * @param userSignupDto DTO containing user registration info.
     * @return The created User entity.
     */
    @Override
    @Transactional
    public User registerUser(UserSignupDto userSignupDto) {
        if (userRepository.existsByPhoneNumber(userSignupDto.getPhoneNumber())) {
            throw new ResourceConflictException("User", "phoneNumber", userSignupDto.getPhoneNumber());
        }

        User user = new User();
        user.setPhoneNumber(userSignupDto.getPhoneNumber());
        user.setName(userSignupDto.getName());
        user.setPasswordHash(passwordService.hashPassword(userSignupDto.getPassword()));

        return userRepository.save(user);
    }
    
    /**
     * Initiates the login process by verifying credentials and sending an OTP.
     * @param loginInitiateDto DTO containing login credentials.
     */
    @Override
    public void initiateLogin(LoginInitiateDto loginInitiateDto) {
        User user = userRepository.findByPhoneNumber(loginInitiateDto.getPhoneNumber())
                .orElseThrow(() -> new ApiException("Invalid credentials.", HttpStatus.UNAUTHORIZED));

        if (!passwordService.comparePassword(loginInitiateDto.getPassword(), user.getPasswordHash())) {
            throw new ApiException("Invalid credentials.", HttpStatus.UNAUTHORIZED);
        }

        String otp = otpService.generateOtp();
        String otpHash = passwordService.hashPassword(otp);

        LoginAttempt attempt = new LoginAttempt(
                user.getPhoneNumber(),
                otpHash,
                LocalDateTime.now().plusMinutes(otpValidityMinutes),
                0
        );
        loginAttemptRepository.save(attempt);

        otpService.sendOtp(user.getPhoneNumber(), otp);
    }

    /**
     * Completes the login process by verifying the OTP and issuing a JWT.
     * @param loginVerifyDto DTO containing phone number and OTP.
     * @return A JWT string.
     */
    @Override
    public String completeLogin(LoginVerifyDto loginVerifyDto) {
        LoginAttempt attempt = loginAttemptRepository.findByPhoneNumber(loginVerifyDto.getPhoneNumber())
                .orElseThrow(() -> new ApiException("Invalid or expired OTP.", HttpStatus.UNAUTHORIZED));

        if (LocalDateTime.now().isAfter(attempt.getExpiresAt())) {
            loginAttemptRepository.deleteByPhoneNumber(loginVerifyDto.getPhoneNumber());
            throw new ApiException("Invalid or expired OTP.", HttpStatus.UNAUTHORIZED);
        }

        if (attempt.getAttempts() >= maxOtpAttempts) {
             loginAttemptRepository.deleteByPhoneNumber(loginVerifyDto.getPhoneNumber());
            throw new ApiException("Maximum OTP attempts exceeded.", HttpStatus.UNAUTHORIZED);
        }

        if (!passwordService.comparePassword(loginVerifyDto.getOtp(), attempt.getOtpHash())) {
            attempt.setAttempts(attempt.getAttempts() + 1);
            loginAttemptRepository.save(attempt);
            throw new ApiException("Invalid or expired OTP.", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByPhoneNumber(loginVerifyDto.getPhoneNumber())
                .orElseThrow(() -> new ApiException("User not found during OTP verification.", HttpStatus.INTERNAL_SERVER_ERROR));
        
        loginAttemptRepository.deleteByPhoneNumber(loginVerifyDto.getPhoneNumber());

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return tokenService.generateToken(userPrincipal);
    }
}