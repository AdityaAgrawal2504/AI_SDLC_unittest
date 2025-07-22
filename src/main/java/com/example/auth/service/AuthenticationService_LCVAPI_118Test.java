package com.example.auth.service;

import com.example.auth.dto.LoginRequest_LCVAPI_104;
import com.example.auth.dto.LoginSuccessResponse_LCVAPI_105;
import com.example.auth.enums.ErrorCode_LCVAPI_107;
import com.example.auth.enums.UserStatus_LCVAPI_108;
import com.example.auth.exception.CustomApiException_LCVAPI_114;
import com.example.auth.model.User_LCVAPI_109;
import com.example.auth.repository.UserRepository_LCVAPI_110;
import com.example.auth.util.RequestIdUtil_LCVAPI_111;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationService_LCVAPI_118Test {

    @Mock
    private UserRepository_LCVAPI_110 userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OtpService_LCVAPI_116 otpService;

    @InjectMocks
    private AuthenticationService_LCVAPI_118 authenticationService;

    private User_LCVAPI_109 activeUser;
    private User_LCVAPI_109 lockedUser;
    private User_LCVAPI_109 inactiveUser;
    private LoginRequest_LCVAPI_104 validLoginRequest;
    private static final String MOCKED_REQUEST_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        activeUser = new User_LCVAPI_109(UUID.randomUUID(), "+14155552671", "encodedPassword", UserStatus_LCVAPI_108.ACTIVE, 0, OffsetDateTime.now(), OffsetDateTime.now());
        lockedUser = new User_LCVAPI_109(UUID.randomUUID(), "+15005550002", "encodedPassword", UserStatus_LCVAPI_108.LOCKED, 5, OffsetDateTime.now(), OffsetDateTime.now());
        inactiveUser = new User_LCVAPI_109(UUID.randomUUID(), "+15005550001", "encodedPassword", UserStatus_LCVAPI_108.INACTIVE, 0, OffsetDateTime.now(), OffsetDateTime.now());

        validLoginRequest = new LoginRequest_LCVAPI_104(activeUser.getPhoneNumber(), "rawPassword");

        // Set maxFailedAttempts using ReflectionTestUtils for @Value field
        ReflectionTestUtils.setField(authenticationService, "maxFailedAttempts", 5);

        // Mock RequestIdUtil.get() to return a predictable value for test assertions
        Mockito.mockStatic(com.example.auth.util.RequestIdUtil_LCVAPI_111.class).when(com.example.auth.util.RequestIdUtil_LCVAPI_111::get).thenReturn(MOCKED_REQUEST_ID);
    }

    @Test
    @DisplayName("Should successfully authenticate an active user and send OTP")
    void loginWithCredentials_ActiveUser_Success() {
        when(userRepository.findByPhoneNumber(activeUser.getPhoneNumber())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword", activeUser.getPassword())).thenReturn(true);
        doNothing().when(otpService).sendOtp(activeUser.getPhoneNumber());
        when(userRepository.save(any(User_LCVAPI_109.class))).thenReturn(activeUser); // For reset attempts

        LoginSuccessResponse_LCVAPI_105 response = authenticationService.loginWithCredentials(validLoginRequest);

        assertNotNull(response);
        assertEquals("OTP sent successfully to the registered phone number.", response.getMessage());
        assertEquals(MOCKED_REQUEST_ID, response.getRequestId());
        verify(userRepository, times(1)).findByPhoneNumber(activeUser.getPhoneNumber());
        verify(passwordEncoder, times(1)).matches("rawPassword", activeUser.getPassword());
        verify(otpService, times(1)).sendOtp(activeUser.getPhoneNumber());
        verify(userRepository, times(1)).save(activeUser); // Verify reset attempts saved
        assertEquals(0, activeUser.getFailedLoginAttempts());
    }

    @Test
    @DisplayName("Should throw INVALID_CREDENTIALS if phone number not found")
    void loginWithCredentials_PhoneNumberNotFound_ThrowsInvalidCredentials() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        CustomApiException_LCVAPI_114 thrown = assertThrows(CustomApiException_LCVAPI_114.class, () -> {
            authenticationService.loginWithCredentials(new LoginRequest_LCVAPI_104("+19999999999", "anyPassword"));
        });

        assertEquals(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS, thrown.getErrorCode());
        verify(userRepository, times(1)).findByPhoneNumber(anyString());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(otpService);
    }

    @Test
    @DisplayName("Should increment failed attempts and throw INVALID_CREDENTIALS for wrong password")
    void loginWithCredentials_WrongPassword_IncrementsFailedAttemptsAndThrowsInvalidCredentials() {
        activeUser.setFailedLoginAttempts(2); // Simulate existing failed attempts
        when(userRepository.findByPhoneNumber(activeUser.getPhoneNumber())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword", activeUser.getPassword())).thenReturn(false);
        when(userRepository.save(any(User_LCVAPI_109.class))).thenReturn(activeUser); // For updating failed attempts

        CustomApiException_LCVAPI_114 thrown = assertThrows(CustomApiException_LCVAPI_114.class, () -> {
            authenticationService.loginWithCredentials(validLoginRequest);
        });

        assertEquals(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS, thrown.getErrorCode());
        assertEquals(3, activeUser.getFailedLoginAttempts()); // Failed attempts should be incremented
        verify(userRepository, times(1)).findByPhoneNumber(activeUser.getPhoneNumber());
        verify(passwordEncoder, times(1)).matches("rawPassword", activeUser.getPassword());
        verify(userRepository, times(1)).save(activeUser);
        verifyNoInteractions(otpService);
    }

    @Test
    @DisplayName("Should lock account after max failed attempts and throw INVALID_CREDENTIALS")
    void loginWithCredentials_MaxFailedAttemptsReached_LocksAccountAndThrowsInvalidCredentials() {
        activeUser.setFailedLoginAttempts(maxFailedAttempts - 1); // One attempt away from locking
        when(userRepository.findByPhoneNumber(activeUser.getPhoneNumber())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword", activeUser.getPassword())).thenReturn(false);
        when(userRepository.save(any(User_LCVAPI_109.class))).thenReturn(activeUser); // For updating failed attempts and status

        CustomApiException_LCVAPI_114 thrown = assertThrows(CustomApiException_LCVAPI_114.class, () -> {
            authenticationService.loginWithCredentials(validLoginRequest);
        });

        assertEquals(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS, thrown.getErrorCode());
        assertEquals(maxFailedAttempts, activeUser.getFailedLoginAttempts());
        assertEquals(UserStatus_LCVAPI_108.LOCKED, activeUser.getStatus()); // Account should be locked
        verify(userRepository, times(1)).save(activeUser);
    }

    @Test
    @DisplayName("Should throw ACCOUNT_LOCKED if user account is locked")
    void loginWithCredentials_LockedUser_ThrowsAccountLocked() {
        when(userRepository.findByPhoneNumber(lockedUser.getPhoneNumber())).thenReturn(Optional.of(lockedUser));

        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104(lockedUser.getPhoneNumber(), "anyPassword");

        CustomApiException_LCVAPI_114 thrown = assertThrows(CustomApiException_LCVAPI_114.class, () -> {
            authenticationService.loginWithCredentials(request);
        });

        assertEquals(ErrorCode_LCVAPI_107.ACCOUNT_LOCKED, thrown.getErrorCode());
        verify(userRepository, times(1)).findByPhoneNumber(lockedUser.getPhoneNumber());
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(userRepository); // Ensure no save attempts
        verifyNoInteractions(otpService);
    }

    @Test
    @DisplayName("Should throw ACCOUNT_INACTIVE if user account is inactive")
    void loginWithCredentials_InactiveUser_ThrowsAccountInactive() {
        when(userRepository.findByPhoneNumber(inactiveUser.getPhoneNumber())).thenReturn(Optional.of(inactiveUser));

        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104(inactiveUser.getPhoneNumber(), "anyPassword");

        CustomApiException_LCVAPI_114 thrown = assertThrows(CustomApiException_LCVAPI_114.class, () -> {
            authenticationService.loginWithCredentials(request);
        });

        assertEquals(ErrorCode_LCVAPI_107.ACCOUNT_INACTIVE, thrown.getErrorCode());
        verify(userRepository, times(1)).findByPhoneNumber(inactiveUser.getPhoneNumber());
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(userRepository); // Ensure no save attempts
        verifyNoInteractions(otpService);
    }

    @Test
    @DisplayName("Should throw OTP_SERVICE_UNAVAILABLE if OTP service fails")
    void loginWithCredentials_OtpServiceFails_ThrowsOtpServiceUnavailable() {
        when(userRepository.findByPhoneNumber(activeUser.getPhoneNumber())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword", activeUser.getPassword())).thenReturn(true);
        doThrow(new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE))
                .when(otpService).sendOtp(activeUser.getPhoneNumber());
        when(userRepository.save(any(User_LCVAPI_109.class))).thenReturn(activeUser); // For reset attempts

        CustomApiException_LCVAPI_114 thrown = assertThrows(CustomApiException_LCVAPI_114.class, () -> {
            authenticationService.loginWithCredentials(validLoginRequest);
        });

        assertEquals(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE, thrown.getErrorCode());
        verify(userRepository, times(1)).findByPhoneNumber(activeUser.getPhoneNumber());
        verify(passwordEncoder, times(1)).matches("rawPassword", activeUser.getPassword());
        verify(otpService, times(1)).sendOtp(activeUser.getPhoneNumber());
        verify(userRepository, times(1)).save(activeUser); // Verify reset attempts were saved before OTP call
        assertEquals(0, activeUser.getFailedLoginAttempts()); // Should be reset on successful password match
    }

    @Test
    @DisplayName("Should not reset failed attempts if already zero")
    void loginWithCredentials_FailedAttemptsAlreadyZero_NoSave() {
        activeUser.setFailedLoginAttempts(0); // Already zero
        when(userRepository.findByPhoneNumber(activeUser.getPhoneNumber())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword", activeUser.getPassword())).thenReturn(true);
        doNothing().when(otpService).sendOtp(activeUser.getPhoneNumber());

        authenticationService.loginWithCredentials(validLoginRequest);

        verify(userRepository, never()).save(activeUser); // Save should not be called if attempts are zero
    }
}
src/test/java/com/example/auth/service/OtpService_LCVAPI_116Test.java