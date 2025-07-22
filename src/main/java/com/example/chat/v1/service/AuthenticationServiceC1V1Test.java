package com.example.chat.v1.service;

import com.example.chat.v1.domain.UserC1V1;
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
class AuthenticationServiceC1V1Test {

    @Mock
    private MockDataRepositoryC1V1 mockDataRepository;

    @InjectMocks
    private AuthenticationServiceC1V1 authenticationService;

    private UserC1V1 testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserC1V1("user_test123", "Test User", "http://test.com/avatar.png");
    }

    @Test
    void getUserIdFromToken_ValidToken_ReturnsUserId() {
        // Arrange
        String validToken = testUser.getId(); // Mock implementation assumes token is user ID
        when(mockDataRepository.findUserById(validToken)).thenReturn(Optional.of(testUser));

        // Act
        Optional<String> result = authenticationService.getUserIdFromToken(validToken);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get());
        verify(mockDataRepository, times(1)).findUserById(validToken);
    }

    @Test
    void getUserIdFromToken_InvalidToken_ReturnsEmptyOptional() {
        // Arrange
        String invalidToken = "invalid_token";
        when(mockDataRepository.findUserById(invalidToken)).thenReturn(Optional.empty());

        // Act
        Optional<String> result = authenticationService.getUserIdFromToken(invalidToken);

        // Assert
        assertFalse(result.isPresent());
        verify(mockDataRepository, times(1)).findUserById(invalidToken);
    }

    @Test
    void getUserIdFromToken_NullToken_ReturnsEmptyOptional() {
        // Arrange
        String nullToken = null;
        when(mockDataRepository.findUserById(nullToken)).thenReturn(Optional.empty());

        // Act
        Optional<String> result = authenticationService.getUserIdFromToken(nullToken);

        // Assert
        assertFalse(result.isPresent());
        verify(mockDataRepository, times(1)).findUserById(nullToken);
    }
}
```
```java
// src/test/java/com/example/chat/v1/service/ChatLogicServiceC1V1Test.java