package com.example.auth.config;

import com.example.auth.util.RequestIdUtil_LCVAPI_111;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A servlet filter that generates a unique request ID for each incoming request.
 */
@Component
public class RequestIdFilter_LCVAPI_112 extends OncePerRequestFilter {

    /**
     * Sets a unique request ID before processing the request and clears it after.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            RequestIdUtil_LCVAPI_111.generateAndSet();
            filterChain.doFilter(request, response);
        } finally {
            RequestIdUtil_LCVAPI_111.clear();
        }
    }
}
src/main/java/com/example/auth/config/SecurityConfig_LCVAPI_113.java