package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class. Defines beans and security rules for the application.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig_F4B8 {

    /**
     * Provides a BCrypt password encoder bean for securely hashing passwords.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security rules for the application.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disabling CSRF as this is a stateless API
            .authorizeRequests()
            .antMatchers("/users/register", "/h2-console/**", "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll() // Permit access to registration and docs
            .anyRequest().authenticated(); // Secure other endpoints if any

        // Required for H2 console
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
```
```java
src/main/java/com/example/util/LoggingAspect_F4B8.java