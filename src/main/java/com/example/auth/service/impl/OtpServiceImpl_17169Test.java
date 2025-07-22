package com.example.auth.service.impl;

import com.example.auth.entity.OtpRecord_17169;
import com.example.auth.enums.ErrorCode_AuthVerifyOtp_17169;
import com.example.auth.exception.ApplicationException_AuthVerifyOtp_17169;
import com.example.auth.repository.OtpRepository_17169;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceImpl_17169Test {

    @Mock
    private OtpRepository_17169 otpRepository;

    @InjectMocks
    private OtpServiceImpl_17169 otpService;

    private final String phoneNumber = "+1234567890";
    private final String otpCode = "123456";
    private final String userId = "user123";
    private OtpRecord_17169 activeOtp;
    private final int MAX_ATTEMPTS = 3;

    @BeforeEach
    void setUp() {
        // Manually set the @Value field using ReflectionTestUtils
        ReflectionTestUtils.setField(otpService, "maxAttempts", MAX_ATTEMPTS);

        activeOtp = new OtpRecord_17169(phoneNumber, otpCode, ZonedDateTime.now().plusMinutes(5), userId);
        activeOtp.setId(UUID.randomUUID());
        activeOtp.setAttempts(0);
        activeOtp.setUsed(false);
    }

    @Test
    void findAndValidateActiveOtp_Success() {
        when(otpRepository.findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber))
                .thenReturn(Optional.of(activeOtp));

        OtpRecord_17169 result = otpService.findAndValidateActiveOtp(phoneNumber, otpCode);

        assertNotNull(result);
        assertEquals(activeOtp, result);
        verify(otpRepository, never()).save(any(OtpRecord_17169.class)); // No save on success
    }

    @Test
    void findAndValidateActiveOtp_OtpNotFound() {
        when(otpRepository.findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber))
                .thenReturn(Optional.empty());

        ApplicationException_AuthVerifyOtp_17169 exception = assertThrows(ApplicationException_AuthVerifyOtp_17169.class, () -> {
            otpService.findAndValidateActiveOtp(phoneNumber, otpCode);
        });

        assertEquals(ErrorCode_AuthVerifyOtp_17169.OTP_NOT_FOUND, exception.getErrorCode());
        verify(otpRepository, never()).save(any(OtpRecord_17169.class));
    }

    @Test
    void findAndValidateActiveOtp_TooManyAttempts() {
        activeOtp.setAttempts(MAX_ATTEMPTS);
        when(otpRepository.findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber))
                .thenReturn(Optional.of(activeOtp));

        ApplicationException_AuthVerifyOtp_17169 exception = assertThrows(ApplicationException_AuthVerifyOtp_17169.class, () -> {
            otpService.findAndValidateActiveOtp(phoneNumber, otpCode);
        });

        assertEquals(ErrorCode_AuthVerifyOtp_17169.TOO_MANY_ATTEMPTS, exception.getErrorCode());
        verify(otpRepository, never()).save(any(OtpRecord_17169.class));
    }

    @Test
    void findAndValidateActiveOtp_OtpExpired() {
        activeOtp.setExpiresAt(ZonedDateTime.now().minusMinutes(1)); // Set to expired
        when(otpRepository.findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber))
                .thenReturn(Optional.of(activeOtp));
        when(otpRepository.save(any(OtpRecord_17169.class))).thenReturn(activeOtp); // For invalidateOtp call

        ApplicationException_AuthVerifyOtp_17169 exception = assertThrows(ApplicationException_AuthVerifyOtp_17169.class, () -> {
            otpService.findAndValidateActiveOtp(phoneNumber, otpCode);
        });

        assertEquals(ErrorCode_AuthVerifyOtp_17169.OTP_EXPIRED, exception.getErrorCode());
        assertTrue(activeOtp.isUsed()); // Should be marked as used
        verify(otpRepository, times(1)).save(activeOtp);
    }

    @Test
    void findAndValidateActiveOtp_IncorrectOtp() {
        String incorrectOtp = "654321";
        when(otpRepository.findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber))
                .thenReturn(Optional.of(activeOtp));
        when(otpRepository.save(any(OtpRecord_17169.class))).thenReturn(activeOtp); // For incrementing attempts

        ApplicationException_AuthVerifyOtp_17169 exception = assertThrows(ApplicationException_AuthVerifyOtp_17169.class, () -> {
            otpService.findAndValidateActiveOtp(phoneNumber, incorrectOtp);
        });

        assertEquals(ErrorCode_AuthVerifyOtp_17169.OTP_INCORRECT, exception.getErrorCode());
        assertEquals(1, activeOtp.getAttempts()); // Attempts should be incremented
        verify(otpRepository, times(1)).save(activeOtp);
    }

    @Test
    void invalidateOtp_Success() {
        otpService.invalidateOtp(activeOtp);

        assertTrue(activeOtp.isUsed());
        verify(otpRepository, times(1)).save(activeOtp);
    }
}
src/test/java/com/example/auth/service/impl/AuthServiceImpl_17169Test.java