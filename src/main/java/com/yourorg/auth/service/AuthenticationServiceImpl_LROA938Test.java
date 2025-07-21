package com.yourorg.auth.service;

import com.yourorg.auth.dto.request.LoginRequest_LROA938;
import com.yourorg.auth.dto.response.LoginSuccessResponse_LROA938;
import com.yourorg.auth.enums.ErrorCode_LROA938;
import com.yourorg.auth.enums.UserStatus_LROA938;
import com.yourorg.auth.exception.ApplicationException_LROA938;
import com.yourorg.auth.model.User_LROA938;
import com.yourorg.auth.repository.UserRepository_LROA938;
import com.yourorg.auth.service.impl.AuthenticationServiceImpl_LROA938;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AuthenticationServiceImpl class.
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImpl_LROA938Test {

    @Mock
    private UserRepository_LROA938 userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService_LROA938 jwtService;

    @Mock
    private OtpService_LROA938 otpService;

    @InjectMocks
    private AuthenticationServiceImpl_LROA938 authenticationService;

    private LoginRequest_LROA938 loginRequest;
    private User_LROA938 activeUser;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest_LROA938("+14155552671", "good-password");
        activeUser = User_LROA938.builder()
                .id(1L)
                .phone("+14155552671")
                .password("hashed-password")
                .status(UserStatus_LROA938.ACTIVE)
                .build();
    }

    /**
     * Tests the successful login and OTP request flow.
     */
    @Test
    void loginAndRequestOtp_Success() {
        // Arrange
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), activeUser.getPassword())).thenReturn(true);
        when(jwtService.generateOtpSessionToken(activeUser.getPhone())).thenReturn("test-jwt-token");
        doNothing().when(otpService).sendOtp(activeUser.getPhone());

        // Act
        LoginSuccessResponse_LROA938 response = authenticationService.loginAndRequestOtp(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("test-jwt-token", response.getOtpSessionToken());
        verify(userRepository).findByPhone(loginRequest.getPhone());
        verify(passwordEncoder).matches(loginRequest.getPassword(), activeUser.getPassword());
        verify(jwtService).generateOtpSessionToken(activeUser.getPhone());
        verify(otpService).sendOtp(activeUser.getPhone());
    }

    /**
     * Tests the case where the user is not found.
     */
    @Test
    void loginAndRequestOtp_UserNotFound() {
        // Arrange
        when(userRepository.findByPhone(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        ApplicationException_LROA938 exception = assertThrows(ApplicationException_LROA938.class,
                () -> authenticationService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCode_LROA938.USER_NOT_FOUND, exception.getErrorCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateOtpSessionToken(anyString());
        verify(otpService, never()).sendOtp(anyString());
    }

    /**
     * Tests the case where the provided password is incorrect.
     */
    @Test
    void loginAndRequestOtp_InvalidCredentials() {
        // Arrange
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), activeUser.getPassword())).thenReturn(false);

        // Act & Assert
        ApplicationException_LROA938 exception = assertThrows(ApplicationException_LROA938.class,
                () -> authenticationService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCode_LROA938.INVALID_CREDENTIALS, exception.getErrorCode());
        verify(jwtService, never()).generateOtpSessionToken(anyString());
        verify(otpService, never()).sendOtp(anyString());
    }

    /**
     * Tests the case where the user's account is locked.
     */
    @Test
    void loginAndRequestOtp_AccountLocked() {
        // Arrange
        activeUser.setStatus(UserStatus_LROA938.LOCKED);
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(activeUser));

        // Act & Assert
        ApplicationException_LROA938 exception = assertThrows(ApplicationException_LROA938.class,
                () -> authenticationService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCode_LROA938.ACCOUNT_LOCKED, exception.getErrorCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    /**
     * Tests the case where the OTP service fails to send the message.
     */
    @Test
    void loginAndRequestOtp_OtpServiceFailure() {
        // Arrange
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), activeUser.getPassword())).thenReturn(true);
        doThrow(new ApplicationException_LROA938(ErrorCode_LROA938.OTP_SERVICE_FAILURE))
                .when(otpService).sendOtp(activeUser.getPhone());

        // Act & Assert
        ApplicationException_LROA938 exception = assertThrows(ApplicationException_LROA938.class,
                () -> authenticationService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCode_LROA938.OTP_SERVICE_FAILURE, exception.getErrorCode());
        verify(jwtService).generateOtpSessionToken(anyString()); // Token generation happens before OTP sending
    }
}
```
```java
// src/test/java/com/yourorg/auth/controller/LoginController_LROA938Test.java