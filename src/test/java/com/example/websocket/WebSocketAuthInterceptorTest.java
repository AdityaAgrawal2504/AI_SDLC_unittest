package com.example.websocket;

import com.example.service.ITokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebSocketAuthInterceptorTest {

    @Mock
    private ITokenService tokenService;

    @InjectMocks
    private WebSocketAuthInterceptor interceptor;

    @Test
    void beforeHandshake_withValidToken_shouldReturnTrueAndSetUserId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("token", "valid.token");
        UUID userId = UUID.randomUUID();

        when(tokenService.validateToken("valid.token")).thenReturn(true);
        when(tokenService.getUserIdFromToken("valid.token")).thenReturn(userId);

        Map<String, Object> attributes = new HashMap<>();
        boolean result = interceptor.beforeHandshake(new ServletServerHttpRequest(request), null, null, attributes);

        assertThat(result).isTrue();
        assertThat(attributes.get("userId")).isEqualTo(userId);
    }

    @Test
    void beforeHandshake_withInvalidToken_shouldReturnFalse() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("token", "invalid.token");

        when(tokenService.validateToken("invalid.token")).thenReturn(false);

        Map<String, Object> attributes = new HashMap<>();
        boolean result = interceptor.beforeHandshake(new ServletServerHttpRequest(request), null, null, attributes);

        assertThat(result).isFalse();
        assertThat(attributes).isEmpty();
    }
}