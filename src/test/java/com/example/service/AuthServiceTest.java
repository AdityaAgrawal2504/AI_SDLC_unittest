package com.example.service;

import com.example.dto.request.UserSignupRequest;
import com.example.model.User;
import com.example.repository.IUserRepository;
import com.example.service.exception.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private IUserRepository userRepository;
    @Mock private IPasswordService passwordService;
    @Mock private IOtpService otpService;
    @Mock private ITokenService tokenService;
    @Mock private com.example.service.logging.IEventLogger eventLogger;

    @InjectMocks private AuthService authService;

    @Test
    void signUp_whenUserDoesNotExist_shouldCreateUser() {
        UserSignupRequest request = new UserSignupRequest();
        request.setPhoneNumber("+1234567890");
        request.setPassword("password");

        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());
        when(passwordService.hashPassword("password")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        assertNotNull(authService.signUp(request));
        verify(userRepository).save(any(User.class));
        verify(eventLogger).log(eq("UserCreated"), anyString());
    }

    @Test
    void signUp_whenUserExists_shouldThrowConflictException() {
        UserSignupRequest request = new UserSignupRequest();
        request.setPhoneNumber("+1234567890");
        
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(new User()));

        assertThrows(ConflictException.class, () -> authService.signUp(request));
        verify(userRepository, never()).save(any());
    }
}