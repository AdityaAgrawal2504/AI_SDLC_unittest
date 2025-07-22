package com.example.conversation.api.config;

import com.example.conversation.api.dto.ErrorResponseFCA1;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

/**
 * Configures application security, including endpoint protection and authentication entry points.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigFCA1 {

    private final ObjectMapper objectMapper;

    public SecurityConfigFCA1(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Defines the security filter chain for HTTP requests.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/conversations/**").authenticated() // Secure the /conversations endpoint
                .anyRequest().permitAll() // Allow other requests (e.g., for health checks)
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint())
            );
        // In a real app, you would add a JWT filter here:
        // http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Creates a custom authentication entry point to handle 401 Unauthorized errors.
     *
     * @return An AuthenticationEntryPoint that writes a custom JSON error response.
     */
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ErrorResponseFCA1 errorResponse = new ErrorResponseFCA1(
                    "UNAUTHENTICATED",
                    "Authentication credentials were not provided or are invalid.",
                    null // No specific details for generic authentication error
                );
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
        };
    }
}
src/main/java/com/example/conversation/api/dto/ParticipantFCA1.java