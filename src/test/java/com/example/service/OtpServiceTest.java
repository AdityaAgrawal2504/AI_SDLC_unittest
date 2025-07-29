package com.example.service;

import com.example.model.LoginAttempt;
import com.example.model.User;
import com.example.repository.ILoginAttemptRepository;
import com.example.service.exception.UnauthorizedException;
import com.example.service.provider.ISmsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock private ILoginAttemptRepository loginAttemptRepository;
    @Mock private IPasswordService passwordService;
    @Mock private ISmsProvider smsProvider;

    @InjectMocks private OtpService otpService;

    @Test
    void generateAndSendOtp_shouldSaveAttemptAndSendSms() {
        User user = new User();
        user.setPhoneNumber("+123");
        
        when(passwordService.hashPassword(anyString())).thenReturn("hashedOtp");
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenAnswer(i -> i.getArguments()[0]);

        UUID attemptId = otpService.generateAndSendOtp(user);

        assertNotNull(attemptId);
        verify(loginAttemptRepository).save(any(LoginAttempt.class));
        verify(smsProvider).sendSms(eq("+123"), anyString());
    }

    @Test
    void verifyOtp_withValidOtp_shouldReturnUser() {
        UUID attemptId = UUID.randomUUID();
        User user = new User();
        LoginAttempt attempt = LoginAttempt.builder()
                .id(attemptId)
                .user(user)
                .otpHash("hashedOtp")
                .expiresAt(Instant.now().plusSeconds(60))
                .isVerified(false)
                .build();

        when(loginAttemptRepository.findById(attemptId)).thenReturn(Optional.of(attempt));
        when(passwordService.comparePassword("123456", "hashedOtp")).thenReturn(true);

        User resultUser = otpService.verifyOtp(attemptId, "123456");

        assertNotNull(resultUser);
        verify(loginAttemptRepository).save(any(LoginAttempt.class));
    }

    @Test
    void verifyOtp_withInvalidOtp_shouldThrowUnauthorizedException() {
        UUID attemptId = UUID.randomUUID();
        LoginAttempt attempt = LoginAttempt.builder()
                .expiresAt(Instant.now().plusSeconds(60))
                .otpHash("hashedOtp")
                .build();
        
        when(loginAttemptRepository.findById(attemptId)).thenReturn(Optional.of(attempt));
        when(passwordService.comparePassword("wrong-otp", "hashedOtp")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> otpService.verifyOtp(attemptId, "wrong-otp"));
    }
}