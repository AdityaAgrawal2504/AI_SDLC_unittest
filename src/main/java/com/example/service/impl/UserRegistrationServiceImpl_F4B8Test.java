package com.example.service.impl;

import com.example.dto.UserRegistrationRequest_F4B8;
import com.example.dto.UserRegistrationResponse_F4B8;
import com.example.entity.User_F4B8;
import com.example.exception.PasswordHashingException_F4B8;
import com.example.exception.UserAlreadyExistsException_F4B8;
import com.example.repository.UserRepository_F4B8;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImpl_F4B8Test {

    @Mock
    private UserRepository_F4B8 userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationServiceImpl_F4B8 userRegistrationService;

    private UserRegistrationRequest_F4B8 request;
    private User_F4B8 newUser;
    private String phoneNumber = "1234567890";
    private String plainPassword = "securePassword123";
    private String hashedPassword = "$2a$10$abcdefghijklmnopqrstuvwxyZ"; // A typical BCrypt hash

    @BeforeEach
    void setUp() {
        request = new UserRegistrationRequest_F4B8(phoneNumber, plainPassword);

        newUser = new User_F4B8();
        newUser.setId(UUID.randomUUID());
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPasswordHash(hashedPassword);
    }

    @Test
    void registerUser_Success() {
        // Given
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(plainPassword)).thenReturn(hashedPassword);
        when(userRepository.save(any(User_F4B8.class))).thenReturn(newUser);

        // When
        UserRegistrationResponse_F4B8 response = userRegistrationService.registerUser(request);

        // Then
        assertNotNull(response);
        assertEquals(newUser.getId().toString(), response.getUserId());
        assertEquals("User registered successfully.", response.getMessage());

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(passwordEncoder, times(1)).encode(plainPassword);
        verify(userRepository, times(1)).save(argThat(user ->
                user.getPhoneNumber().equals(phoneNumber) &&
                user.getPasswordHash().equals(hashedPassword)
        ));
    }

    @Test
    void registerUser_UserAlreadyExists() {
        // Given
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(newUser));

        // When/Then
        UserAlreadyExistsException_F4B8 thrown = assertThrows(UserAlreadyExistsException_F4B8.class, () -> {
            userRegistrationService.registerUser(request);
        });

        assertEquals("A user with the phone number " + phoneNumber + " already exists.", thrown.getMessage());
        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User_F4B8.class));
    }

    @Test
    void registerUser_PasswordHashingFailure() {
        // Given
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(plainPassword)).thenThrow(new RuntimeException("Hashing failed internally"));

        // When/Then
        PasswordHashingException_F4B8 thrown = assertThrows(PasswordHashingException_F4B8.class, () -> {
            userRegistrationService.registerUser(request);
        });

        assertEquals("Failed to hash user password.", thrown.getMessage());
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof RuntimeException);

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(passwordEncoder, times(1)).encode(plainPassword);
        verify(userRepository, never()).save(any(User_F4B8.class));
    }

    @Test
    void registerUser_UserRepositorySaveFails() {
        // Given
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(plainPassword)).thenReturn(hashedPassword);
        when(userRepository.save(any(User_F4B8.class))).thenThrow(new RuntimeException("Database save error"));

        // When/Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userRegistrationService.registerUser(request);
        });

        assertEquals("Database save error", thrown.getMessage());

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(passwordEncoder, times(1)).encode(plainPassword);
        verify(userRepository, times(1)).save(any(User_F4B8.class));
    }
}
```
```java
src/test/java/com/example/controller/UserRegistrationController_F4B8Test.java