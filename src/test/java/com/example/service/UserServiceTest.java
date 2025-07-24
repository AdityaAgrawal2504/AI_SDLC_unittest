package com.example.service;

import com.example.dto.UserRegistrationRequest;
import com.example.dto.UserResponse;
import com.example.exception.DuplicateResourceException;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IPasswordService passwordService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_Success() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setPhoneNumber("+15551234567");
        request.setPassword("Password123!");

        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
        when(passwordService.hashPassword(request.getPassword())).thenReturn("hashedPassword");

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash("hashedPassword")
                .createdAt(OffsetDateTime.now())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getId());
        assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsDuplicateResourceException() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setPhoneNumber("+15551234567");
        request.setPassword("Password123!");

        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.registerUser(request));

        verify(userRepository, never()).save(any(User.class));
    }
}