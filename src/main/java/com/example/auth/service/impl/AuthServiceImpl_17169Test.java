package com.example.auth.service.impl;

import com.example.auth.dto.request.VerifyOtpRequest_17169;
import com.example.auth.dto.response.VerifyOtpSuccessResponse_17169;
import com.example.auth.entity.OtpRecord_17169;
import com.example.auth.enums.ErrorCode_AuthVerifyOtp_17169;
import com.example.auth.exception.ApplicationException_AuthVerifyOtp_17169;
import com.example.auth.security.JwtTokenProvider_17169;
import com.example.auth.service.OtpService_17169;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImpl_17169Test {

    @Mock
    private OtpService_17169 otpService;

    @Mock
    private JwtTokenProvider_17169 jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl_17169 authService;

    private VerifyOtpRequest_17169 request;
    private OtpRecord_17169 otpRecord;
    private final String phoneNumber = "+1234567890";
    private final String otpCode = "123456";
    private final String userId = "user123";
    private final long TOKEN_EXPIRATION_SECONDS = 3600L;

    @BeforeEach
    void setUp() {
        // Manually set the @Value field using ReflectionTestUtils
        ReflectionTestUtils.setField(authService, "tokenExpirationSeconds", TOKEN_EXPIRATION_SECONDS);

        request = new VerifyOtpRequest_17169();
        request.setPhoneNumber(phoneNumber);
        request.setOtp(otpCode);

        otpRecord = new OtpRecord_17169(phoneNumber, otpCode, ZonedDateTime.now().plusMinutes(5), userId);
        otpRecord.setId(UUID.randomUUID());
        otpRecord.setAttempts(0);
        otpRecord.setUsed(false);
    }

    @Test
    void verifyOtpAndCreateSession_Success() {
        when(otpService.findAndValidateActiveOtp(phoneNumber, otpCode)).thenReturn(otpRecord);
        when(jwtTokenProvider.createToken(userId, phoneNumber)).thenReturn("mockSessionToken");
        doNothing().when(otpService).invalidateOtp(otpRecord);

        VerifyOtpSuccessResponse_17169 response = authService.verifyOtpAndCreateSession(request);

        assertNotNull(response);
        assertEquals("mockSessionToken", response.getSessionToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(TOKEN_EXPIRATION_SECONDS, response.getExpiresIn());

        verify(otpService, times(1)).findAndValidateActiveOtp(phoneNumber, otpCode);
        verify(jwtTokenProvider, times(1)).createToken(userId, phoneNumber);
        verify(otpService, times(1)).invalidateOtp(otpRecord);
    }

    @Test
    void verifyOtpAndCreateSession_ApplicationExceptionFromOtpService() {
        ApplicationException_AuthVerifyOtp_17169 expectedException =
                new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.OTP_NOT_FOUND);

        when(otpService.findAndValidateActiveOtp(phoneNumber, otpCode)).thenThrow(expectedException);

        ApplicationException_AuthVerifyOtp_17169 thrownException = assertThrows(ApplicationException_AuthVerifyOtp_17169.class, () -> {
            authService.verifyOtpAndCreateSession(request);
        });

        assertEquals(expectedException, thrownException);
        verify(otpService, times(1)).findAndValidateActiveOtp(phoneNumber, otpCode);
        verify(jwtTokenProvider, never()).createToken(anyString(), anyString());
        verify(otpService, never()).invalidateOtp(any(OtpRecord_17169.class));
    }

    @Test
    void verifyOtpAndCreateSession_UnexpectedExceptionDuringTokenCreation() {
        when(otpService.findAndValidateActiveOtp(phoneNumber, otpCode)).thenReturn(otpRecord);
        when(jwtTokenProvider.createToken(userId, phoneNumber)).thenThrow(new RuntimeException("JWT error"));

        ApplicationException_AuthVerifyOtp_17169 thrownException = assertThrows(ApplicationException_AuthVerifyOtp_17169.class, () -> {
            authService.verifyOtpAndCreateSession(request);
        });

        assertEquals(ErrorCode_AuthVerifyOtp_17169.SESSION_CREATION_FAILED, thrownException.getErrorCode());
        verify(otpService, times(1)).findAndValidateActiveOtp(phoneNumber, otpCode);
        verify(jwtTokenProvider, times(1)).createToken(userId, phoneNumber);
        verify(otpService, never()).invalidateOtp(any(OtpRecord_17169.class)); // OTP should not be invalidated
    }
}
src/test/java/com/example/auth/controller/AuthController_17169Test.java