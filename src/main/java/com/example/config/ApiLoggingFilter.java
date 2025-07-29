package com.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filter to log incoming requests, including processing time.
 */
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(ApiLoggingFilter.class);

    /**
     * Logs the start and end of each request, including the total time taken.
     * @param request The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param filterChain The filter chain.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("Request start: {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Request end: {} {} - Status: {} - Duration: {}ms",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
        }
    }
}