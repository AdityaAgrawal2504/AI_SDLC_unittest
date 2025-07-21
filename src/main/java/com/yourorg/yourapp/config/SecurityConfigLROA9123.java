package com.yourorg.yourapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures application security, including password encoding and HTTP security rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigLROA9123 {

    /**
     * Defines a PasswordEncoder bean for securely hashing and verifying passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoderLROA9123() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChainLROA9123(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF as we are using token-based auth
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/auth/**").permitAll() // Allow access to auth endpoints
                 .requestMatchers("/h2-console/**").permitAll() // Allow H2 console for dev
                .anyRequest().authenticated()
            );
        
        // This is needed to make H2 console work with Spring Security
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
```
src/main/java/com/yourorg/yourapp/util/JwtUtilLROA9123.java
```java