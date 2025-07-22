package com.example.chat.v1.service;

import com.example.chat.v1.grpc.ServerToClientEvent;
import com.example.chat.v1.logging.AbstractLoggerC1V1;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages active user streams for dispatching real-time events.
 */
@Service
public class UserStreamManagerC1V1 {

    private final Map<String, StreamObserver<ServerToClientEvent>> activeStreams = new ConcurrentHashMap<>();
    private final AbstractLoggerC1V1 logger;

    public UserStreamManagerC1V1(AbstractLoggerC1V1 logger) {
        this.logger = logger;
    }

    /**
     * Registers a new stream for a user.
     */
    public void registerStream(String userId, StreamObserver<ServerToClientEvent> streamObserver) {
        activeStreams.put(userId, streamObserver);
        logger.logInfo("Stream registered", Map.of("userId", userId, "activeStreamsCount", activeStreams.size()));
    }

    /**
     * Removes a user's stream, typically on disconnection.
     */
    public void removeStream(String userId) {
        activeStreams.remove(userId);
        logger.logInfo("Stream removed", Map.of("userId", userId, "activeStreamsCount", activeStreams.size()));
    }

    /**
     * Sends an event to a specific user if they are connected.
     */
    public void sendToUser(String userId, ServerToClientEvent event) {
        StreamObserver<ServerToClientEvent> observer = activeStreams.get(userId);
        if (observer != null) {
            try {
                observer.onNext(event);
                logger.logInfo("Event sent to user", Map.of("userId", userId, "eventType", event.getEventCase().toString()));
            } catch (Exception e) {
                logger.logError("Error sending event to user, removing stream", Map.of("userId", userId, "eventType", event.getEventCase().toString()), e);
                removeStream(userId);
            }
        } else {
            logger.logWarn("Attempted to send event to disconnected user", Map.of("userId", userId, "eventType", event.getEventCase().toString()));
        }
    }
}
```
```java
// src/main/java/com/example/chat/v1/service/EventDispatchServiceC1V1.java