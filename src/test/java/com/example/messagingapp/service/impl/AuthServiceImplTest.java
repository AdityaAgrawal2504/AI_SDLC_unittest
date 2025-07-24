package com.example.messagingapp.service.impl;

import com.example.messagingapp.dto.AuthTokenResponse;
import com.example.messagingapp.dto.LoginInitiateRequest;
import com.example.messagingapp.dto.LoginVerifyRequest;
import com.example.messagingapp.exception.ApiException;
import com.example.messagingapp.model.User;
import com.example.messagingapp.service.OTPService;
import com.example.messagingapp.service.TokenService;
import com.example.messagingapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OTPService otpService;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private LoginInitiateRequest loginInitiateRequest;
    private LoginVerifyRequest loginVerifyRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .phoneNumber("+1234567890")
                .password("hashedPassword")
                .build();

        loginInitiateRequest = new LoginInitiateRequest(testUser.getPhoneNumber(), "rawPassword");
        loginVerifyRequest = new LoginVerifyRequest(testUser.getPhoneNumber(), "123456");
    }

    /**
     * Tests successful login initiation.
     */
    @Test
    void initiateLogin_success() {
        when(userService.findByPhoneNumber(loginInitiateRequest.getPhoneNumber())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginInitiateRequest.getPassword(), testUser.getPassword())).thenReturn(true);
        doNothing().when(otpService).generateAndSend(testUser.getPhoneNumber());

        assertDoesNotThrow(() -> authService.initiateLogin(loginInitiateRequest));

        verify(otpService).generateAndSend(testUser.getPhoneNumber());
    }

    /**
     * Tests login initiation with an invalid password.
     */
    @Test
    void initiateLogin_invalidPassword_throwsApiException() {
        when(userService.findByPhoneNumber(loginInitiateRequest.getPhoneNumber())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginInitiateRequest.getPassword(), testUser.getPassword())).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> authService.initiateLogin(loginInitiateRequest));
        assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
        verify(otpService, never()).generateAndSend(anyString());
    }

    /**
     * Tests successful login verification.
     */
    @Test
    void verifyLogin_success() {
        when(otpService.verify(loginVerifyRequest.getPhoneNumber(), loginVerifyRequest.getOtp())).thenReturn(true);
        when(userService.findByPhoneNumber(loginVerifyRequest.getPhoneNumber())).thenReturn(Optional.of(testUser));
        when(tokenService.generateAccessToken(testUser)).thenReturn("test.token");

        AuthTokenResponse response = authService.verifyLogin(loginVerifyRequest);

        assertNotNull(response);
        assertEquals("test.token", response.getAccessToken());
    }

    /**
     * Tests login verification with an invalid OTP.
     */
    @Test
    void verifyLogin_invalidOtp_throwsApiException() {
        when(otpService.verify(loginVerifyRequest.getPhoneNumber(), loginVerifyRequest.getOtp())).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> authService.verifyLogin(loginVerifyRequest));
        assertEquals("OTP_INVALID_OR_EXPIRED", exception.getErrorCode());
        verify(userService, never()).findByPhoneNumber(anyString());
        verify(tokenService, never()).generateAccessToken(any(User.class));
    }
}