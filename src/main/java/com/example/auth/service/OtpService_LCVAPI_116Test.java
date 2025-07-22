package com.example.auth.service;

import com.example.auth.enums.ErrorCode_LCVAPI_107;
import com.example.auth.exception.CustomApiException_LCVAPI_114;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpService_LCVAPI_116Test {

    @InjectMocks
    private OtpService_LCVAPI_116 otpService;

    // We need to mock ThreadLocalRandom to control its behavior
    // This is typically done outside of Mockito context using a utility class
    // or by making ThreadLocalRandom accessible via a method in OtpService
    // For this test, we'll use Mockito's static mocking capabilities.
    private MockedStatic<ThreadLocalRandom> mockedRandom;


    @BeforeEach
    void setUp() {
        mockedRandom = mockStatic(ThreadLocalRandom.class);
    }

    @Test
    @DisplayName("Should successfully send OTP when no random failure occurs")
    void sendOtp_NoFailure_Success() {
        String phoneNumber = "+12345678900";
        // Ensure nextInt(10) does not return 0, simulating no failure
        when(ThreadLocalRandom.current()).thenReturn(mock(ThreadLocalRandom.class));
        when(ThreadLocalRandom.current().nextInt(10)).thenReturn(1); // Any non-zero value

        assertDoesNotThrow(() -> otpService.sendOtp(phoneNumber));
        // Verify a log.info message containing "Mock OTP Dispatch" was logged
        // This is harder to test directly without a custom Appender for Log4j2 in test
        // but we can infer success if no exception is thrown
    }

    @Test
    @DisplayName("Should throw CustomApiException_LCVAPI_114 with OTP_SERVICE_UNAVAILABLE on simulated failure")
    void sendOtp_SimulatedFailure_ThrowsOtpServiceUnavailable() {
        String phoneNumber = "+12345678900";
        // Force nextInt(10) to return 0, simulating failure
        when(ThreadLocalRandom.current()).thenReturn(mock(ThreadLocalRandom.class));
        when(ThreadLocalRandom.current().nextInt(10)).thenReturn(0); // Force failure

        CustomApiException_LCVAPI_114 thrown = assertThrows(CustomApiException_LCVAPI_114.class, () -> {
            otpService.sendOtp(phoneNumber);
        });

        assertEquals(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE, thrown.getErrorCode());
        assertEquals(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE.getDefaultMessage(), thrown.getMessage());
    }

    // Clean up the static mock after each test
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        mockedRandom.close();
    }
}
pom.xml