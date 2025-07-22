package com.example.config;

import com.example.util.RequestIdFilterLCVAPI_1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

/**
 * Configures application-wide security settings, including password encoding and HTTP security rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigLCVAPI_1 {

    /**
     * Defines the password encoder bean to be used for hashing passwords.
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoderLCVAPI() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Defines a filter for generating and managing request IDs.
     * @return A RequestIdFilter instance.
     */
    @Bean
    public RequestIdFilterLCVAPI_1 requestIdFilter() {
        return new RequestIdFilterLCVAPI_1();
    }

    /**
     * Configures HTTP security rules for the application.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChainLCVAPI(HttpSecurity http) throws Exception {
        http
            // Add the custom request ID filter before the security context filter.
            .addFilterBefore(requestIdFilter(), SecurityContextPersistenceFilter.class)
            // Disable CSRF as this is a stateless API.
            .csrf().disable()
            // Configure authorization rules.
            .authorizeRequests()
            // Permit all requests to the /auth/login endpoint.
            .antMatchers("/auth/login", "/h2-console/**").permitAll()
            // All other requests should be authenticated (example, can be changed).
            .anyRequest().authenticated()
            .and()
            // Configure session management to be stateless.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // H2 console frame options
            .headers().frameOptions().sameOrigin();

        return http.build();
    }
}
```

src/main/java/com/example/util/RequestIdUtilLCVAPI_1.java
```java