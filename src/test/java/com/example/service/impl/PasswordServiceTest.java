package com.example.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        passwordService = new PasswordService(passwordEncoder);
    }

    @Test
    void hashPassword_shouldReturnHashedString() {
        String plainPassword = "password123";
        String hashedPassword = passwordService.hashPassword(plainPassword);

        assertThat(hashedPassword).isNotNull();
        assertThat(hashedPassword).isNotEqualTo(plainPassword);
    }

    @Test
    void comparePassword_whenMatches_shouldReturnTrue() {
        String plainPassword = "password123";
        String hashedPassword = passwordService.hashPassword(plainPassword);

        boolean matches = passwordService.comparePassword(plainPassword, hashedPassword);
        assertThat(matches).isTrue();
    }

    @Test
    void comparePassword_whenNotMatches_shouldReturnFalse() {
        String plainPassword = "password123";
        String wrongPassword = "password456";
        String hashedPassword = passwordService.hashPassword(plainPassword);

        boolean matches = passwordService.comparePassword(wrongPassword, hashedPassword);
        assertThat(matches).isFalse();
    }
}