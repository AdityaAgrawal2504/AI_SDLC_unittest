package com.example.auth.initiate.api_IA_9F3E.service;

import com.example.auth.initiate.api_IA_9F3E.constants.UserStatus_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginRequestDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginResponseDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.entity.User_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.AccountLockedException_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.InvalidCredentialsException_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.UserNotFoundException_IA_9F3E;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImpl_IA_9F3ETest {

    @Mock
    private IUserService_IA_9F3E userService;

    @Mock
    private IPasswordService_IA_9F3E passwordService;

    @Mock
    private IOtpService_IA_9F3E otpService;

    @InjectMocks
    private AuthServiceImpl_IA_9F3E authService;

    private InitiateLoginRequestDTO_IA_9F3E request;
    private User_IA_9F3E activeUser;

    @BeforeEach
    void setUp() {
        request = new InitiateLoginRequestDTO_IA_9F3E("1234567890", "password123");
        activeUser = User_IA_9F3E.builder()
                .id("user-id-1")
                .phoneNumber("1234567890")
                .passwordHash("hashedPassword")
                .status(UserStatus_IA_9F3E.ACTIVE)
                .build();
    }

    @Test
    void initiateLogin_whenSuccessful_shouldReturnResponseDTO() {
        when(userService.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(activeUser));
        when(passwordService.verifyPassword(request.getPassword(), activeUser.getPasswordHash())).thenReturn(true);
        doNothing().when(otpService).sendLoginOtp(anyString(), anyString());

        InitiateLoginResponseDTO_IA_9F3E response = authService.initiateLogin(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("OTP sent successfully to your registered mobile number.", response.getMessage());
        assertNotNull(response.getTransactionId());
        verify(otpService, times(1)).sendLoginOtp(eq(activeUser.getPhoneNumber()), anyString());
    }

    @Test
    void initiateLogin_whenUserNotFound_shouldThrowUserNotFoundException() {
        when(userService.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException_IA_9F3E.class, () -> {
            authService.initiateLogin(request);
        });

        verify(passwordService, never()).verifyPassword(anyString(), anyString());
        verify(otpService, never()).sendLoginOtp(anyString(), anyString());
    }

    @Test
    void initiateLogin_whenPasswordIsIncorrect_shouldThrowInvalidCredentialsException() {
        when(userService.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(activeUser));
        when(passwordService.verifyPassword(request.getPassword(), activeUser.getPasswordHash())).thenReturn(false);

        assertThrows(InvalidCredentialsException_IA_9F3E.class, () -> {
            authService.initiateLogin(request);
        });

        verify(otpService, never()).sendLoginOtp(anyString(), anyString());
    }

    @Test
    void initiateLogin_whenAccountIsLocked_shouldThrowAccountLockedException() {
        User_IA_9F3E lockedUser = User_IA_9F3E.builder()
                .status(UserStatus_IA_9F3E.LOCKED)
                .phoneNumber(request.getPhoneNumber())
                .build();

        when(userService.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(lockedUser));

        assertThrows(AccountLockedException_IA_9F3E.class, () -> {
            authService.initiateLogin(request);
        });

        verify(passwordService, never()).verifyPassword(anyString(), anyString());
        verify(otpService, never()).sendLoginOtp(anyString(), anyString());
    }
}
```
```java