package com.fetchmessagesapi.security;

import com.fetchmessagesapi.constants.ApiConstantsFMA1;
import com.fetchmessagesapi.util.JwtUtilFMA1;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

/**
 * Filter to process JWT authentication on each request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilterFMA1 extends OncePerRequestFilter {

    private final JwtUtilFMA1 jwtUtil;

    /**
     * Processes the incoming request to validate the JWT and set the security context.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(ApiConstantsFMA1.HEADER_AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(ApiConstantsFMA1.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(ApiConstantsFMA1.BEARER_PREFIX.length());
        try {
            if (jwtUtil.validateToken(jwt)) {
                UUID userId = jwtUtil.extractUserId(jwt);
                // The principal is the user's UUID. Credentials are null as it's token-based auth.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.emptyList()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Let the AuthenticationEntryPoint handle the error response
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/security/SecurityConfigFMA1.java