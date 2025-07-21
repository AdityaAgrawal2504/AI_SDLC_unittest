package com.fetchconversations.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures application security, such as protecting endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigFCA911 {

    /**
     * Defines the security filter chain for HTTP requests.
     * @param http The HttpSecurity to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // For this example, we disable CSRF and allow all requests to the API path.
        // In a real application, you would add authentication (e.g., .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt))
        // and configure authorization rules.
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests(authz -> authz
                .antMatchers("/v1/conversations/**").authenticated() // Secure the endpoint
                .anyRequest().permitAll() // Allow other paths (like health checks)
            )
            // This is a placeholder for actual authentication.
            // In a real scenario, a filter would validate a JWT/token and populate the SecurityContext.
            .httpBasic(); // Using HTTP Basic for simplicity.

        return http.build();
    }
}
```
```java
// src/main/java/com/fetchconversations/api/util/AuthUtilFCA911.java