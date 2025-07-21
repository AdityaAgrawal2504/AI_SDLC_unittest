package com.fetchmessagesapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetchmessagesapi.dto.ErrorResponseFMA1;
import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handles failed authentication attempts by returning a 401 Unauthorized response.
 */
@Component
public class JwtAuthenticationEntryPointFMA1 implements AuthenticationEntryPoint {

    /**
     * Commences an authentication scheme.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseFMA1 errorResponse = new ErrorResponseFMA1(
            ErrorCodeFMA1.AUTHENTICATION_FAILED,
            "Authentication token is missing, malformed, or invalid."
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/logging/LoggingAspectFMA1.java