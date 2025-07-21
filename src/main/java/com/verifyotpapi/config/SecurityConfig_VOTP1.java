package com.verifyotpapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures application security, including password encoding and public endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig_VOTP1 {

    /**
     * Defines the security filter chain, disabling CSRF and permitting access to the auth endpoint.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/auth/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            // Required for H2 console frame to be displayed
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    /**
     * Provides a BCrypt password encoder bean for hashing OTPs.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
```java
// src/main/java/com/verifyotpapi/util/ErrorCode_VOTP1.java