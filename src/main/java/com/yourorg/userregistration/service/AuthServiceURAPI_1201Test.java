package com.yourorg.userregistration.service;

import com.yourorg.userregistration.dto.UserRegistrationRequestURAPI_1201;
import com.yourorg.userregistration.dto.UserRegistrationResponseURAPI_1201;
import com.yourorg.userregistration.exception.UserAlreadyExistsExceptionURAPI_1201;
import com.yourorg.userregistration.model.UserURAPI_1201;
import com.yourorg.userregistration.repository.IUserRepositoryURAPI_1201;
import com.yourorg.userregistration.security.IPasswordHasherURAPI_1201;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceURAPI_1201Test {

    @Mock
    private IUserRepositoryURAPI_1201 userRepository;

    @Mock
    private IPasswordHasherURAPI_1201 passwordHasher;

    @InjectMocks
    private AuthServiceURAPI_1201 authService;

    private UserRegistrationRequestURAPI_1201 request;
    private UserURAPI_1201 savedUser;

    @BeforeEach
    void setUp() {
        request = new UserRegistrationRequestURAPI_1201();
        request.setPhoneNumber("+14155552671");
        request.setPassword("P@ssw0rd123!");

        savedUser = new UserURAPI_1201();
        savedUser.setId(UUID.randomUUID());
        savedUser.setPhoneNumber(request.getPhoneNumber());
        savedUser.setPasswordHash("hashedPassword");
    }

    /**
     * Tests successful user registration.
     */
    @Test
    void register_whenUserDoesNotExist_shouldRegisterSuccessfully() {
        // Arrange
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserURAPI_1201.class))).thenReturn(savedUser);

        // Act
        UserRegistrationResponseURAPI_1201 response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getUserId());
        assertEquals("REGISTRATION_SUCCESSFUL", response.getStatus());
        verify(userRepository, times(1)).findByPhoneNumber(request.getPhoneNumber());
        verify(passwordHasher, times(1)).hash(request.getPassword());
        verify(userRepository, times(1)).save(any(UserURAPI_1201.class));
    }

    /**
     * Tests registration failure when a user with the same phone number already exists.
     */
    @Test
    void register_whenUserAlreadyExists_shouldThrowException() {
        // Arrange
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(new UserURAPI_1201()));

        // Act & Assert
        assertThrows(UserAlreadyExistsExceptionURAPI_1201.class, () -> authService.register(request));
        verify(userRepository, times(1)).findByPhoneNumber(request.getPhoneNumber());
        verify(passwordHasher, never()).hash(anyString());
        verify(userRepository, never()).save(any(UserURAPI_1201.class));
    }

    /**
     * Tests for unexpected exceptions during repository save operation.
     */
    @Test
    void register_whenRepositorySaveFails_shouldThrowException() {
        // Arrange
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserURAPI_1201.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.register(request));
    }
}
```
```java
// src/test/java/com/yourorg/userregistration/controller/AuthControllerURAPI_1201Test.java