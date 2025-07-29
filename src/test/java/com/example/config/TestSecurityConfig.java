package com.example.config;

import com.example.repository.IUserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.service.ITokenService;
import org.springframework.context.annotation.Import;

/**
 * Test configuration to provide mocks needed by SecurityConfig for WebMvcTest slices.
 */
@TestConfiguration
@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean
    private ITokenService tokenService;

    @MockBean
    private IUserRepository userRepository;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
}