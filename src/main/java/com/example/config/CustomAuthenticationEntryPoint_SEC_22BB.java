package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom authentication entry point to handle unauthenticated access attempts.
 * Returns a 401 Unauthorized response in a consistent JSON format.
 */
@Component
public class CustomAuthenticationEntryPoint_SEC_22BB implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Commences an authentication scheme.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("statusCode", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("reason", "Unauthorized");
        body.put("message", "Authentication token is missing, invalid, or expired.");
        
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
```
```java
//
// Filename: src/main/java/com/example/controller/UserAuthController_UATH_1001.java
//