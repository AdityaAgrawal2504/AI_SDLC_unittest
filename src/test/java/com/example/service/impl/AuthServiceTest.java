package com.example.service.impl;

import com.example.dto.LoginInitiateDto;
import com.example.dto.LoginVerifyDto;
import com.example.dto.UserSignupDto;
import com.example.exception.ApiException;
import com.example.exception.ResourceConflictException;
import com.example.model.LoginAttempt;
import com.example.model.User;
import com.example.repository.ILoginAttemptRepository;
import com.example.repository.IUserRepository;
import com.example.service.IOtpService;
import com.example.service.IPasswordService;
import com.example.service.ITokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock private IUserRepository userRepository;
    @Mock private ILoginAttemptRepository loginAttemptRepository;
    @Mock private IPasswordService passwordService;
    @Mock private IOtpService otpService;
    @Mock private ITokenService tokenService;
    
    @InjectMocks private AuthService authService;

    private UserSignupDto signupDto;
    private User user;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, loginAttemptRepository, passwordService, otpService, tokenService, 3, 5);
        
        signupDto = new UserSignupDto();
        signupDto.setPhoneNumber("+1234567890");
        signupDto.setPassword("ValidPass1!");
        signupDto.setName("Test User");

        user = new User();
        user.setPhoneNumber(signupDto.getPhoneNumber());
        user.setPasswordHash("hashedPassword");
    }

    @Test
    void registerUser_whenPhoneNumberNotExists_shouldSucceed() {
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(passwordService.hashPassword(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User registeredUser = authService.registerUser(signupDto);

        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getPhoneNumber()).isEqualTo(signupDto.getPhoneNumber());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_whenPhoneNumberExists_shouldThrowConflict() {
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);
        assertThrows(ResourceConflictException.class, () -> authService.registerUser(signupDto));
    }

    @Test
    void initiateLogin_whenCredentialsValid_shouldSendOtp() {
        LoginInitiateDto loginDto = new LoginInitiateDto();
        loginDto.setPhoneNumber(user.getPhoneNumber());
        loginDto.setPassword("plainPassword");

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(passwordService.comparePassword("plainPassword", "hashedPassword")).thenReturn(true);
        when(otpService.generateOtp()).thenReturn("123456");
        when(passwordService.hashPassword("123456")).thenReturn("hashedOtp");
        
        authService.initiateLogin(loginDto);
        
        verify(loginAttemptRepository, times(1)).save(any(LoginAttempt.class));
        verify(otpService, times(1)).sendOtp(anyString(), anyString());
    }

    @Test
    void completeLogin_whenOtpValid_shouldReturnToken() {
        LoginVerifyDto verifyDto = new LoginVerifyDto();
        verifyDto.setPhoneNumber(user.getPhoneNumber());
        verifyDto.setOtp("123456");

        LoginAttempt attempt = new LoginAttempt(user.getPhoneNumber(), "hashedOtp", LocalDateTime.now().plusMinutes(5), 0);
        
        when(loginAttemptRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(attempt));
        when(passwordService.comparePassword("123456", "hashedOtp")).thenReturn(true);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(any())).thenReturn("jwt.token");

        String token = authService.completeLogin(verifyDto);

        assertThat(token).isEqualTo("jwt.token");
        verify(loginAttemptRepository, times(1)).deleteByPhoneNumber(anyString());
    }
    
    @Test
    void completeLogin_whenOtpInvalid_shouldThrowApiExeption() {
        LoginVerifyDto verifyDto = new LoginVerifyDto();
        verifyDto.setPhoneNumber(user.getPhoneNumber());
        verifyDto.setOtp("wrong-otp");
        
        LoginAttempt attempt = new LoginAttempt(user.getPhoneNumber(), "hashedOtp", LocalDateTime.now().plusMinutes(5), 0);

        when(loginAttemptRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(attempt));
        when(passwordService.comparePassword(anyString(), anyString())).thenReturn(false);

        assertThrows(ApiException.class, () -> authService.completeLogin(verifyDto));
        verify(loginAttemptRepository, never()).deleteByPhoneNumber(anyString());
    }
}