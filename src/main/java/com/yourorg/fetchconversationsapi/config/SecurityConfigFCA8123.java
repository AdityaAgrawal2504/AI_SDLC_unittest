package com.yourorg.fetchconversationsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configures web security for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigFCA8123 {

    /**
     * Defines the security filter chain to protect API endpoints.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
            .authorizeHttpRequests(authz -> authz
                // A real app would have more granular security, e.g., based on roles
                .requestMatchers("/v1/conversations").authenticated() // Secure the main endpoint
                .anyRequest().permitAll() // Allow access to other endpoints like health checks
            )
            .httpBasic(withDefaults()) // Use HTTP Basic Auth for simplicity; a real app would use JWT etc.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless session policy

        return http.build();
    }
}
```
src/main/java/com/yourorg/fetchconversationsapi/logging/LoggerServiceFCA8123.java