package com.auth.api.service;

import com.auth.api.dto.UserRegAPI_UserRegistrationRequest_33A1;
import com.auth.api.dto.UserRegAPI_UserRegistrationResponse_33A1;
import com.auth.api.enums.UserRegAPI_RegistrationStatus_33A1;
import com.auth.api.exception.UserRegAPI_ConflictException_33A1;
import com.auth.api.model.UserRegAPI_UserEntity_33A1;
import com.auth.api.repository.UserRegAPI_UserRepository_33A1;
import com.auth.api.service.hasher.UserRegAPI_PasswordHasher_33A1;
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
class UserRegAPI_AuthServiceImpl_33A1Test {

    @Mock
    private UserRegAPI_UserRepository_33A1 userRepository;

    @Mock
    private UserRegAPI_PasswordHasher_33A1 passwordHasher;

    @InjectMocks
    private UserRegAPI_AuthServiceImpl_33A1 authService;

    private UserRegAPI_UserRegistrationRequest_33A1 request;
    private UserRegAPI_UserEntity_33A1 savedUser;

    @BeforeEach
    void setUp() {
        request = new UserRegAPI_UserRegistrationRequest_33A1("+14155552671", "P@ssw0rd123!");
        
        savedUser = new UserRegAPI_UserEntity_33A1();
        savedUser.setId(UUID.randomUUID());
        savedUser.setPhoneNumber(request.getPhoneNumber());
        savedUser.setPasswordHash("hashedPassword");
    }

    /**
     * Positive test case: Tests successful user registration.
     */
    @Test
    void register_shouldSucceed_whenPhoneNumberIsUnique() {
        // Arrange
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserRegAPI_UserEntity_33A1.class))).thenReturn(savedUser);

        // Act
        UserRegAPI_UserRegistrationResponse_33A1 response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getUserId());
        assertEquals(UserRegAPI_RegistrationStatus_33A1.REGISTRATION_SUCCESSFUL, response.getStatus());
        verify(userRepository, times(1)).findByPhoneNumber(request.getPhoneNumber());
        verify(passwordHasher, times(1)).hash(request.getPassword());
        verify(userRepository, times(1)).save(any(UserRegAPI_UserEntity_33A1.class));
    }

    /**
     * Negative test case: Tests registration failure when phone number already exists.
     */
    @Test
    void register_shouldThrowConflictException_whenPhoneNumberExists() {
        // Arrange
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(new UserRegAPI_UserEntity_33A1()));

        // Act & Assert
        UserRegAPI_ConflictException_33A1 exception = assertThrows(UserRegAPI_ConflictException_33A1.class, () -> {
            authService.register(request);
        });

        assertEquals("A user with this phone number already exists.", exception.getMessage());
        verify(userRepository, times(1)).findByPhoneNumber(request.getPhoneNumber());
        verify(passwordHasher, never()).hash(anyString());
        verify(userRepository, never()).save(any(UserRegAPI_UserEntity_33A1.class));
    }

    /**
     * Edge case: Tests that a null request throws an exception (handled by controller validation, but good to check service).
     */
    @Test
    void register_shouldThrowException_whenRequestIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> authService.register(null));
    }
}
```
```java
// src/test/java/com/auth/api/controller/UserRegAPI_AuthController_33A1Test.java