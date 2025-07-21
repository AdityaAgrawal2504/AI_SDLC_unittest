package com.yourorg.yourapp.service;

import com.yourorg.yourapp.dto.request.LoginRequestLROA9123;
import com.yourorg.yourapp.dto.response.LoginSuccessResponseLROA9123;
import com.yourorg.yourapp.enums.ErrorCodeLROA9123;
import com.yourorg.yourapp.exception.ApiExceptionLROA9123;
import com.yourorg.yourapp.model.UserLROA9123;
import com.yourorg.yourapp.repository.UserRepositoryLROA9123;
import com.yourorg.yourapp.util.JwtUtilLROA9123;
import com.yourorg.yourapp.util.OtpGeneratorUtilLROA9123;
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

@ExtendWith(MockitoExtension.class)
class AuthServiceImplLROA9123Test {

    @Mock
    private UserRepositoryLROA9123 userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OtpGeneratorUtilLROA9123 otpGenerator;
    @Mock
    private OtpServiceClientLROA9123 otpServiceClient;
    @Mock
    private JwtUtilLROA9123 jwtUtil;

    @InjectMocks
    private AuthServiceImplLROA9123 authService;

    private LoginRequestLROA9123 loginRequest;
    private UserLROA9123 user;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestLROA9123("+14155552671", "MyS3cur3P@ssw0rd!");
        user = UserLROA9123.builder()
            .id(1L)
            .phone("+14155552671")
            .password("hashedPassword")
            .accountLocked(false)
            .build();
    }

    @Test
    void loginAndRequestOtp_Success() {
        // Arrange
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(otpGenerator.generateOtp()).thenReturn("123456");
        doNothing().when(otpServiceClient).sendOtp(anyString(), anyString());
        when(jwtUtil.generateOtpSessionToken(user.getPhone())).thenReturn("fake-jwt-token");

        // Act
        LoginSuccessResponseLROA9123 response = authService.loginAndRequestOtp(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("fake-jwt-token", response.getOtpSessionToken());
        verify(userRepository).findByPhone(loginRequest.getPhone());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(otpGenerator).generateOtp();
        verify(otpServiceClient).sendOtp(user.getPhone(), "123456");
        verify(jwtUtil).generateOtpSessionToken(user.getPhone());
    }

    @Test
    void loginAndRequestOtp_UserNotFound() {
        // Arrange
        when(userRepository.findByPhone(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        ApiExceptionLROA9123 exception = assertThrows(ApiExceptionLROA9123.class,
            () -> authService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCodeLROA9123.USER_NOT_FOUND, exception.getErrorCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loginAndRequestOtp_AccountLocked() {
        // Arrange
        user.setAccountLocked(true);
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(user));

        // Act & Assert
        ApiExceptionLROA9123 exception = assertThrows(ApiExceptionLROA9123.class,
            () -> authService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCodeLROA9123.ACCOUNT_LOCKED, exception.getErrorCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loginAndRequestOtp_InvalidCredentials() {
        // Arrange
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        ApiExceptionLROA9123 exception = assertThrows(ApiExceptionLROA9123.class,
            () -> authService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCodeLROA9123.INVALID_CREDENTIALS, exception.getErrorCode());
        verify(otpServiceClient, never()).sendOtp(anyString(), anyString());
    }

    @Test
    void loginAndRequestOtp_OtpServiceFailure() {
        // Arrange
        when(userRepository.findByPhone(loginRequest.getPhone())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(otpGenerator.generateOtp()).thenReturn("123456");
        doThrow(new ApiExceptionLROA9123(ErrorCodeLROA9123.OTP_SERVICE_FAILURE))
            .when(otpServiceClient).sendOtp(anyString(), anyString());

        // Act & Assert
        ApiExceptionLROA9123 exception = assertThrows(ApiExceptionLROA9123.class,
            () -> authService.loginAndRequestOtp(loginRequest));

        assertEquals(ErrorCodeLROA9123.OTP_SERVICE_FAILURE, exception.getErrorCode());
        verify(jwtUtil, never()).generateOtpSessionToken(anyString());
    }
}
```
src/test/java/com/yourorg/yourapp/controller/AuthControllerV1_LROA9123Test.java
```java