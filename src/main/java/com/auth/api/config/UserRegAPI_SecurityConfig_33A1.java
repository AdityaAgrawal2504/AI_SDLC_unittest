package com.auth.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configures application security, including password encoding and HTTP security rules.
 */
@Configuration
@EnableWebSecurity
public class UserRegAPI_SecurityConfig_33A1 {

    /**
     * Defines the PasswordEncoder bean to be used for hashing passwords.
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disabling CSRF for stateless REST API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/auth/register", "/h2-console/**").permitAll() // Allow public access to registration and H2 console
                .anyRequest().authenticated() // All other requests require authentication
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // For H2 console
            .httpBasic(withDefaults());
        return http.build();
    }
}
```
```java
// src/main/java/com/auth/api/controller/UserRegAPI_AuthController_33A1.java