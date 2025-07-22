package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig_LCVAPI_113 {

    private final RequestIdFilter_LCVAPI_112 requestIdFilter;

    public SecurityConfig_LCVAPI_113(RequestIdFilter_LCVAPI_112 requestIdFilter) {
        this.requestIdFilter = requestIdFilter;
    }

    /**
     * Defines the BCrypt password encoder bean.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security rules. Disables CSRF and allows all requests for this simple API.
     * @param http The HttpSecurity to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            // Required for H2 console frame to be displayed
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            // Add the RequestIdFilter before Spring Security's filters
            .addFilterBefore(requestIdFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
src/main/java/com/example/auth/dto/ErrorResponse_LCVAPI_106.java