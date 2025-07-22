package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures web security, defining which endpoints are public and which are protected.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig_AuthVerifyOtp_17169 {

    /**
     * Defines the security filter chain for HTTP requests.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF as this is a stateless API
                .csrf(AbstractHttpConfigurer::disable)
                // Configure authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Allow unauthenticated access to the OTP verification endpoint and H2 console
                        .requestMatchers("/auth/verify-otp", "/h2-console/**").permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                // Configure session management to be stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Required for H2 console frame to be displayed
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
pom.xml