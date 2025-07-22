package com.example.userregistration.service;

import com.example.userregistration.dto.UserRegistrationRequest_URAPI_1;
import com.example.userregistration.dto.UserRegistrationResponse_URAPI_1;
import com.example.userregistration.entity.User_URAPI_1;
import com.example.userregistration.exception.PasswordHashingException_URAPI_1;
import com.example.userregistration.exception.UserAlreadyExistsException_URAPI_1;
import com.example.userregistration.repository.UserRepository_URAPI_1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationService_URAPI_1Test {

    @Mock
    private UserRepository_URAPI_1 userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationService_URAPI_1 userRegistrationService;

    private UserRegistrationRequest_URAPI_1 request;
    private User_URAPI_1 newUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        request = new UserRegistrationRequest_URAPI_1();
        request.setPhoneNumber("1234567890");
        request.setPassword("SecurePassword123");

        userId = UUID.randomUUID();
        newUser = User_URAPI_1.builder()
                .id(userId)
                .phoneNumber(request.getPhoneNumber())
                .passwordHash("hashedPassword123")
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void registerUser_Success() {
        // Given
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword123");
        when(userRepository.save(any(User_URAPI_1.class))).thenReturn(newUser);

        // When
        UserRegistrationResponse_URAPI_1 response = userRegistrationService.registerUser(request);

        // Then
        assertNotNull(response);
        assertEquals(userId.toString(), response.getUserId());
        assertEquals("User registered successfully.", response.getMessage());
        verify(userRepository, times(1)).existsByPhoneNumber(request.getPhoneNumber());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User_URAPI_1.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException if phone number already exists")
    void registerUser_UserAlreadyExists() {
        // Given
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

        // When/Then
        UserAlreadyExistsException_URAPI_1 exception = assertThrows(UserAlreadyExistsException_URAPI_1.class,
                () -> userRegistrationService.registerUser(request));

        assertEquals("A user with phone number " + request.getPhoneNumber() + " already exists.", exception.getMessage());
        verify(userRepository, times(1)).existsByPhoneNumber(request.getPhoneNumber());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User_URAPI_1.class));
    }

    @Test
    @DisplayName("Should throw PasswordHashingException if password hashing fails")
    void registerUser_PasswordHashingFailure() {
        // Given
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenThrow(new RuntimeException("Hashing error"));

        // When/Then
        PasswordHashingException_URAPI_1 exception = assertThrows(PasswordHashingException_URAPI_1.class,
                () -> userRegistrationService.registerUser(request));

        assertTrue(exception.getMessage().contains("Failed to hash user password."));
        assertTrue(exception.getCause() instanceof RuntimeException);
        verify(userRepository, times(1)).existsByPhoneNumber(request.getPhoneNumber());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, never()).save(any(User_URAPI_1.class));
    }
}
```
src/test/java/com/example/userregistration/controller/UserRegistrationController_URAPI_1Test.java
```java