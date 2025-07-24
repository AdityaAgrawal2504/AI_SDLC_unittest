package com.example.service;

import com.example.dto.RequestOtpRequest;
import com.example.dto.VerifyOtpRequest;
import com.example.dto.VerifyOtpResponse;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.OtpValidationException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private IPasswordService passwordService;
    @Mock
    private IOtpService otpService;
    @Mock
    private ITokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void requestLoginOtp_Success() {
        RequestOtpRequest request = new RequestOtpRequest();
        request.setPhoneNumber("+15551234567");
        request.setPassword("password");

        User user = User.builder().phoneNumber(request.getPhoneNumber()).passwordHash("hashed").build();

        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(user));
        when(passwordService.verifyPassword(request.getPassword(), user.getPasswordHash())).thenReturn(true);

        authService.requestLoginOtp(request);

        verify(otpService, times(1)).generateAndStoreOtp(user.getPhoneNumber());
    }

    @Test
    void requestLoginOtp_UserNotFound() {
        RequestOtpRequest request = new RequestOtpRequest();
        request.setPhoneNumber("+15551234567");

        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.requestLoginOtp(request));
    }

    @Test
    void requestLoginOtp_InvalidPassword() {
        RequestOtpRequest request = new RequestOtpRequest();
        request.setPhoneNumber("+15551234567");
        request.setPassword("wrongPassword");

        User user = User.builder().phoneNumber(request.getPhoneNumber()).passwordHash("hashed").build();

        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(user));
        when(passwordService.verifyPassword(request.getPassword(), user.getPasswordHash())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.requestLoginOtp(request));
    }

    @Test
    void verifyLoginOtp_Success() {
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhoneNumber("+15551234567");
        request.setOtp("123456");

        User user = User.builder().id(UUID.randomUUID()).phoneNumber(request.getPhoneNumber()).build();
        String expectedToken = "jwt-token";

        when(otpService.validateOtp(request.getPhoneNumber(), request.getOtp())).thenReturn(true);
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(user)).thenReturn(expectedToken);

        VerifyOtpResponse response = authService.verifyLoginOtp(request);

        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void verifyLoginOtp_InvalidOtp() {
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhoneNumber("+15551234567");
        request.setOtp("wrong-otp");

        when(otpService.validateOtp(request.getPhoneNumber(), request.getOtp())).thenReturn(false);

        assertThrows(OtpValidationException.class, () -> authService.verifyLoginOtp(request));
    }
}