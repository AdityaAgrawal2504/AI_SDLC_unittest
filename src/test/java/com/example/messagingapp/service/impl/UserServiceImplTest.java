package com.example.messagingapp.service.impl;

import com.example.messagingapp.dto.UserRegistrationRequest;
import com.example.messagingapp.dto.UserResponse;
import com.example.messagingapp.exception.ApiException;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequest registrationRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequest("+1234567890", "password123");
        user = User.builder()
                .id(UUID.randomUUID())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .password("encodedPassword")
                .build();
    }
    
    /**
     * Tests successful user registration.
     */
    @Test
    void registerUser_success() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.registerUser(registrationRequest);

        assertNotNull(response);
        assertEquals(registrationRequest.getPhoneNumber(), response.getPhoneNumber());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    /**
     * Tests registration with an existing phone number.
     */
    @Test
    void registerUser_phoneNumberExists_throwsConflictException() {
        when(userRepository.findByPhoneNumber(registrationRequest.getPhoneNumber())).thenReturn(Optional.of(user));
        
        ApiException exception = assertThrows(ApiException.class, () -> userService.registerUser(registrationRequest));
        
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("PHONE_NUMBER_ALREADY_EXISTS", exception.getErrorCode());
        verify(userRepository, never()).save(any());
    }
    
    /**
     * Tests searching for users by phone number.
     */
    @Test
    void searchUsers_returnsListOfUserResponses() {
        when(userRepository.findByPhoneNumberContaining("234")).thenReturn(Collections.singletonList(user));

        List<UserResponse> responses = userService.searchUsers("234");
        
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(user.getPhoneNumber(), responses.get(0).getPhoneNumber());
    }
}