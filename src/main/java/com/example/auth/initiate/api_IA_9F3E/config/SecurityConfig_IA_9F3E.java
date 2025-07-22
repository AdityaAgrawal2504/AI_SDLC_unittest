package com.example.auth.initiate.api_IA_9F3E.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures security-related beans for the application.
 */
@Configuration
public class SecurityConfig_IA_9F3E {

    /**
     * Provides a BCrypt password encoder bean for hashing and verifying passwords.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder_IA_9F3E() {
        return new BCryptPasswordEncoder();
    }
}
```
```java