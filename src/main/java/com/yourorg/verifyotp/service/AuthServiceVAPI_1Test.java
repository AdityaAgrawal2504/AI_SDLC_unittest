package com.yourorg.verifyotp.service;

import com.yourorg.verifyotp.constants.ErrorCodeVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpRequestVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpSuccessResponseVAPI_1;
import com.yourorg.verifyotp.exception.CustomApiExceptionVAPI_1;
import com.yourorg.verifyotp.model.OtpDataVAPI_1;
import com.yourorg.verifyotp.repository.OtpRepositoryVAPI_1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceVAPI_1Test {

    @Mock
    private OtpRepositoryVAPI_1 otpRepository;

    @Mock
    private ITokenServiceVAPI_1 tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceVAPI_1 authService;

    private VerifyOtpRequestVAPI_1 request;
    private OtpDataVAPI_1 otpData;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceVAPI_1(otpRepository, tokenService, passwordEncoder, 3);
        
        request = new VerifyOtpRequestVAPI_1();
        request.setVerificationToken("valid-token");
        request.setOtpCode("123456");

        otpData = new OtpDataVAPI_1("user-123", "hashed-otp", "valid-token", Instant.now().plus(10, ChronoUnit.MINUTES));
    }

    @Test
    void verifyOtp_Success() {
        when(otpRepository.findByVerificationToken("valid-token")).thenReturn(Optional.of(otpData));
        when(passwordEncoder.matches("123456", "hashed-otp")).thenReturn(true);
        when(tokenService.generateSessionTokens("user-123")).thenReturn(new VerifyOtpSuccessResponseVAPI_1("access-token", 3600, "refresh-token"));

        VerifyOtpSuccessResponseVAPI_1 response = authService.verifyOtp(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        verify(otpRepository, times(1)).delete(otpData);
    }

    @Test
    void verifyOtp_TokenNotFound() {
        when(otpRepository.findByVerificationToken("invalid-token")).thenReturn(Optional.empty());
        request.setVerificationToken("invalid-token");

        CustomApiExceptionVAPI_1 exception = assertThrows(CustomApiExceptionVAPI_1.class, () -> authService.verifyOtp(request));

        assertEquals(ErrorCodeVAPI_1.TOKEN_NOT_FOUND.name(), exception.getErrorCode());
        assertEquals(ErrorCodeVAPI_1.TOKEN_NOT_FOUND.getStatus(), exception.getStatus());
    }

    @Test
    void verifyOtp_TokenExpired() {
        otpData.setExpiresAt(Instant.now().minus(1, ChronoUnit.MINUTES));
        when(otpRepository.findByVerificationToken("valid-token")).thenReturn(Optional.of(otpData));

        CustomApiExceptionVAPI_1 exception = assertThrows(CustomApiExceptionVAPI_1.class, () -> authService.verifyOtp(request));

        assertEquals(ErrorCodeVAPI_1.TOKEN_EXPIRED.name(), exception.getErrorCode());
    }

    @Test
    void verifyOtp_MaxAttemptsReached() {
        otpData.setAttempts(3);
        when(otpRepository.findByVerificationToken("valid-token")).thenReturn(Optional.of(otpData));

        CustomApiExceptionVAPI_1 exception = assertThrows(CustomApiExceptionVAPI_1.class, () -> authService.verifyOtp(request));

        assertEquals(ErrorCodeVAPI_1.MAX_ATTEMPTS_REACHED.name(), exception.getErrorCode());
    }

    @Test
    void verifyOtp_InvalidCode() {
        when(otpRepository.findByVerificationToken("valid-token")).thenReturn(Optional.of(otpData));
        when(passwordEncoder.matches("123456", "hashed-otp")).thenReturn(false);

        CustomApiExceptionVAPI_1 exception = assertThrows(CustomApiExceptionVAPI_1.class, () -> authService.verifyOtp(request));

        assertEquals(ErrorCodeVAPI_1.INVALID_OTP.name(), exception.getErrorCode());
        verify(otpRepository, times(1)).save(any(OtpDataVAPI_1.class));
        assertEquals(1, otpData.getAttempts());
    }
}
```
```java
// Test: AuthControllerVAPI_1Test.java