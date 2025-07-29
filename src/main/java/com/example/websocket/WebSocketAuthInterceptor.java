package com.example.websocket;

import com.example.service.ITokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private static final Logger log = LogManager.getLogger(WebSocketAuthInterceptor.class);
    private final ITokenService tokenService;

    public WebSocketAuthInterceptor(ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Intercepts the WebSocket handshake to authenticate the user via a token in the query parameter.
     * @return true if the handshake should proceed, false otherwise.
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = UriComponentsBuilder.fromHttpRequest(request).build()
                .getQueryParams().getFirst("token");

        if (token != null && tokenService.validateToken(token)) {
            UUID userId = tokenService.getUserIdFromToken(token);
            attributes.put("userId", userId);
            log.info("WebSocket handshake authorized for user {}", userId);
            return true;
        }

        log.warn("WebSocket handshake failed: Invalid or missing token.");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No-op
    }
}