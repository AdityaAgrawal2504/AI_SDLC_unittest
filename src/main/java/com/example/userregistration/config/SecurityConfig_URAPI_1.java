package com.example.userregistration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig_URAPI_1 {

    /**
     * Provides a PasswordEncoder bean for securely hashing passwords.
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder_URAPI_1() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain.
     * Disables CSRF and allows all requests to the registration endpoint.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain_URAPI_1(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/register").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console access
                .anyRequest().authenticated()
            );
        
        // Required for H2 console frames to be displayed
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
```
src/main/java/com/example/userregistration/logging/LogPublisher_URAPI_1.java
```java