src/test/java/com/example/service/impl/OtpService_M3N4OTest.java
package com.example.service.impl;

import com.example.model.Otp_Y9Z0A;
import com.example.model.User_M1N2O;
import com.example.provider.interfaces.ISmsProvider_M9N0O;
import com.example.repository.IOtpRepository_Y1Z0A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpService_M3N4OTest {

    @Mock
    private IOtpRepository_Y1Z0A otpRepository;
    @Mock
    private ISmsProvider_M9N0O smsProvider;

    @InjectMocks
    private OtpService_M3N4O otpService;

    private User_M1N2O user;

    @BeforeEach
    void setUp() {
        user = new User_M1N2O();
        user.setId(UUID.randomUUID());
        user.setPhoneNumber("+15551234567");
    }

    @Test
    void generateAndSendOtp_Success() {
        when(smsProvider.send(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(null));
        when(otpRepository.save(any(Otp_Y9Z0A.class))).thenAnswer(invocation -> invocation.getArgument(0));

        otpService.generateAndSendOtp(user);

        ArgumentCaptor<Otp_Y9Z0A> otpCaptor = ArgumentCaptor.forClass(Otp_Y9Z0A.class);
        verify(otpRepository, times(1)).save(otpCaptor.capture());
        
        Otp_Y9Z0A savedOtp = otpCaptor.getValue();
        assertNotNull(savedOtp);
        assertEquals(user, savedOtp.getUser());
        assertNotNull(savedOtp.getCode());
        assertEquals(6, savedOtp.getCode().length());
        assertTrue(savedOtp.getExpiresAt().isAfter(LocalDateTime.now()));

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(smsProvider, times(1)).send(eq(user.getPhoneNumber()), messageCaptor.capture());
        assertTrue(messageCaptor.getValue().contains(savedOtp.getCode()));
    }

    @Test
    void verifyOtp_Success() {
        String code = "123456";
        Otp_Y9Z0A otp = new Otp_Y9Z0A();
        otp.setUser(user);
        otp.setCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        
        when(otpRepository.findByUserAndCodeAndExpiresAtAfter(eq(user), eq(code), any(LocalDateTime.class))).thenReturn(Optional.of(otp));
        
        boolean result = otpService.verifyOtp(user, code);
        
        assertTrue(result);
        verify(otpRepository, times(1)).delete(otp);
    }

    @Test
    void verifyOtp_Failure_InvalidCode() {
        String code = "111111";
        when(otpRepository.findByUserAndCodeAndExpiresAtAfter(eq(user), eq(code), any(LocalDateTime.class))).thenReturn(Optional.empty());

        boolean result = otpService.verifyOtp(user, code);

        assertFalse(result);
        verify(otpRepository, never()).delete(any());
    }
    
    @Test
    void verifyOtp_Failure_ExpiredCode() {
        // The service logic checks for expiry in the repository query, so this is equivalent to an invalid code.
        String code = "123456";
        when(otpRepository.findByUserAndCodeAndExpiresAtAfter(eq(user), eq(code), any(LocalDateTime.class))).thenReturn(Optional.empty());

        boolean result = otpService.verifyOtp(user, code);
        
        assertFalse(result);
        verify(otpRepository, never()).delete(any());
    }
}