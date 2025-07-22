package com.example.auth.initiate.api_IA_9F3E.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordServiceImpl_IA_9F3ETest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordServiceImpl_IA_9F3E passwordService;

    private String plainPassword;
    private String hashedPassword;

    @BeforeEach
    void setUp() {
        plainPassword = "password123";
        hashedPassword = "hashedPassword123";
    }

    @Test
    void verifyPassword_whenPasswordsMatch_shouldReturnTrue() {
        when(passwordEncoder.matches(plainPassword, hashedPassword)).thenReturn(true);

        boolean result = passwordService.verifyPassword(plainPassword, hashedPassword);

        assertTrue(result);
    }

    @Test
    void verifyPassword_whenPasswordsDoNotMatch_shouldReturnFalse() {
        when(passwordEncoder.matches(plainPassword, hashedPassword)).thenReturn(false);

        boolean result = passwordService.verifyPassword(plainPassword, hashedPassword);

        assertFalse(result);
    }
}
```
```java