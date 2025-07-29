package com.example.config;

import com.example.websocket.WebSocketAuthInterceptor;
import com.example.websocket.WebSocketGateway;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketGateway webSocketGateway;
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    public WebSocketConfig(WebSocketGateway webSocketGateway, WebSocketAuthInterceptor webSocketAuthInterceptor) {
        this.webSocketGateway = webSocketGateway;
        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    }

    /**
     * Registers WebSocket handlers and interceptors.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketGateway, "/ws")
                .addInterceptors(webSocketAuthInterceptor)
                .setAllowedOrigins("*");
    }
}