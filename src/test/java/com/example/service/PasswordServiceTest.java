package com.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordService passwordService;

    @Test
    void hashPassword_shouldCallEncoder() {
        passwordService.hashPassword("rawPass");
        verify(passwordEncoder).encode("rawPass");
    }

    @Test
    void comparePassword_shouldCallMatcher() {
        passwordService.comparePassword("rawPass", "encodedPass");
        verify(passwordEncoder).matches("rawPass", "encodedPass");
    }
}