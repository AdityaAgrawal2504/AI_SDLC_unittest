package com.verifyotpapi.service;

import com.verifyotpapi.config.AppProperties_VOTP1;
import com.verifyotpapi.dto.request.VerifyOtpRequest_VOTP1;
import com.verifyotpapi.dto.response.VerifyOtpSuccessResponse_VOTP1;
import com.verifyotpapi.exception.*;
import com.verifyotpapi.logging.StructuredLogger_VOTP1;
import com.verifyotpapi.model.OtpData_VOTP1;
import com.verifyotpapi.repository.OtpDataRepository_VOTP1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthService_VOTP1Test {

    @Mock
    private OtpDataRepository_VOTP1 otpRepository;
    @Mock
    private ITokenService_VOTP1 tokenService;
    @Mock
    private PasswordHashingService_VOTP1 passwordHashingService;
    @Mock
    private AppProperties_VOTP1 appProperties;
    @Mock
    private StructuredLogger_VOTP1 logger;

    @InjectMocks
    private AuthService_VOTP1 authService;

    private VerifyOtpRequest_VOTP1 request;
    private OtpData_VOTP1 otpData;

    @BeforeEach
    void setUp() {
        request = new VerifyOtpRequest_VOTP1("valid-token", "123456");

        otpData = OtpData_VOTP1.builder()
            .userId("user-123")
            .verificationToken("valid-token")
            .hashedOtp("hashed-otp")
            .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
            .attempts(0)
            .build();

        // Mock app properties
        AppProperties_VOTP1.Otp otpProps = new AppProperties_VOTP1.Otp(3, 5);
        when(appProperties.otp()).thenReturn(otpProps);
    }

    @Test
    void verifyOtp_Success() {
        // Arrange
        when(otpRepository.findByVerificationToken(request.getVerificationToken())).thenReturn(Optional.of(otpData));
        when(passwordHashingService.matches(request.getOtpCode(), otpData.getHashedOtp())).thenReturn(true);
        VerifyOtpSuccessResponse_VOTP1 successResponse = VerifyOtpSuccessResponse_VOTP1.builder().accessToken("access-token").build();
        when(tokenService.generateSessionTokens(otpData.getUserId())).thenReturn(successResponse);

        // Act
        VerifyOtpSuccessResponse_VOTP1 response = authService.verifyOtp(request);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        verify(otpRepository, times(1)).findByVerificationToken(request.getVerificationToken());
        verify(passwordHashingService, times(1)).matches(request.getOtpCode(), otpData.getHashedOtp());
        verify(tokenService, times(1)).generateSessionTokens(otpData.getUserId());
        verify(otpRepository, times(1)).delete(otpData);
    }

    @Test
    void verifyOtp_TokenNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(otpRepository.findByVerificationToken(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException_VOTP1.class, () -> authService.verifyOtp(request));
        verify(otpRepository, times(1)).findByVerificationToken(request.getVerificationToken());
        verify(passwordHashingService, never()).matches(any(), any());
        verify(tokenService, never()).generateSessionTokens(any());
    }

    @Test
    void verifyOtp_TokenExpired_ThrowsTokenExpiredException() {
        // Arrange
        otpData.setExpiresAt(Instant.now().minus(1, ChronoUnit.MINUTES));
        when(otpRepository.findByVerificationToken(request.getVerificationToken())).thenReturn(Optional.of(otpData));

        // Act & Assert
        assertThrows(TokenExpiredException_VOTP1.class, () -> authService.verifyOtp(request));
        verify(otpRepository, times(1)).delete(otpData);
    }

    @Test
    void verifyOtp_MaxAttemptsReached_ThrowsMaxAttemptsExceededException() {
        // Arrange
        otpData.setAttempts(3); // Max attempts is 3 from setup
        when(otpRepository.findByVerificationToken(request.getVerificationToken())).thenReturn(Optional.of(otpData));

        // Act & Assert
        assertThrows(MaxAttemptsExceededException_VOTP1.class, () -> authService.verifyOtp(request));
        verify(otpRepository, times(1)).delete(otpData);
    }

    @Test
    void verifyOtp_InvalidCode_ThrowsInvalidOtpException() {
        // Arrange
        when(otpRepository.findByVerificationToken(request.getVerificationToken())).thenReturn(Optional.of(otpData));
        when(passwordHashingService.matches(request.getOtpCode(), otpData.getHashedOtp())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidOtpException_VOTP1.class, () -> authService.verifyOtp(request));
        verify(otpRepository, times(1)).incrementAttempts(request.getVerificationToken());
        verify(otpRepository, never()).delete(any());
        verify(tokenService, never()).generateSessionTokens(any());
    }
}
```
```java
// src/test/java/com/verifyotpapi/controller/AuthController_VOTP1Test.java