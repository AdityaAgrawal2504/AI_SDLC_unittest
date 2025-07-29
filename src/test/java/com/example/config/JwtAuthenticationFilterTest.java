package com.example.config;

import com.example.model.User;
import com.example.service.ITokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock private ITokenService tokenService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void doFilterInternal_withValidToken_shouldSetAuthentication() throws ServletException, IOException {
        String token = "valid-token";
        String authHeader = "Bearer " + token;
        String userId = UUID.randomUUID().toString();
        UserDetails userDetails = User.builder().id(UUID.fromString(userId)).build();

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.extractUserId(token)).thenReturn(userId);
        when(userDetailsService.loadUserByUsername(userId)).thenReturn(userDetails);
        when(tokenService.isTokenValid(token, userDetails)).thenReturn(true);

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService).extractUserId(token);
        verify(userDetailsService).loadUserByUsername(userId);
        verify(tokenService).isTokenValid(token, userDetails);
        verify(filterChain).doFilter(request, response);
        assert(SecurityContextHolder.getContext().getAuthentication() != null);
    }

    @Test
    void doFilterInternal_withNoToken_shouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenService, userDetailsService);
    }
}