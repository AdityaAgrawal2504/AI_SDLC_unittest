package com.example.service;

import com.example.dto.RequestOtpRequest;
import com.example.dto.VerifyOtpRequest;
import com.example.dto.VerifyOtpResponse;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.OtpValidationException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private static final Logger log = LogManager.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final IPasswordService passwordService;
    private final IOtpService otpService;
    private final ITokenService tokenService;

    /**
     * Validates user credentials and initiates the OTP sending process.
     * @param request DTO containing phone number and password.
     * @throws ResourceNotFoundException if user doesn't exist.
     * @throws InvalidCredentialsException if the password is incorrect.
     */
    @Override
    @Transactional(readOnly = true)
    public void requestLoginOtp(RequestOtpRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with phone number: " + request.getPhoneNumber()));

        if (!passwordService.verifyPassword(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password provided.");
        }

        String otp = otpService.generateAndStoreOtp(user.getPhoneNumber());
        // In a real app, an external service would send the OTP via SMS.
        log.info("Generated OTP {} for user {}", otp, user.getPhoneNumber());
    }

    /**
     * Verifies a user-provided OTP and generates a session token upon success.
     * @param request DTO containing phone number and OTP.
     * @return DTO containing the JWT.
     * @throws OtpValidationException if the OTP is invalid or expired.
     * @throws ResourceNotFoundException if the user doesn't exist.
     */
    @Override
    @Transactional(readOnly = true)
    public VerifyOtpResponse verifyLoginOtp(VerifyOtpRequest request) {
        if (!otpService.validateOtp(request.getPhoneNumber(), request.getOtp())) {
            throw new OtpValidationException("OTP is invalid or has expired.");
        }

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with phone number: " + request.getPhoneNumber()));

        String token = tokenService.generateToken(user);

        return new VerifyOtpResponse(token, "Bearer");
    }
}