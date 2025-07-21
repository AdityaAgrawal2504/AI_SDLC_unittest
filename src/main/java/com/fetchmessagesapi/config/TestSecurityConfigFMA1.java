package com.fetchmessagesapi.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.UUID;

/**
 * Test configuration to mock security for WebMvcTests.
 */
@TestConfiguration
public class TestSecurityConfigFMA1 {

    public static final UUID TEST_USER_ID = UUID.fromString("e9f2f8f8-c2b1-4a1e-843c-ae742d4a6a9b");

    /**
     * Replaces the main security filter chain with a test-friendly version.
     * This version bypasses JWT and uses a mock user.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .authenticationManager(authentication -> {
                // Directly authenticate with our test principal
                PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(TEST_USER_ID, "mock-credentials");
                token.setAuthenticated(true);
                return token;
            });
        return http.build();
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/config/DataInitializerFMA1.java