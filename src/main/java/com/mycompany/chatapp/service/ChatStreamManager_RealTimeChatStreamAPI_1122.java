package com.mycompany.chatapp.service;

import com.mycompany.chatapp.grpc.ServerStreamResponse;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages active client streams and broadcasting of messages.
 */
@Service
public class ChatStreamManager_RealTimeChatStreamAPI_1122 {

    private static final Logger logger = LogManager.getLogger(ChatStreamManager_RealTimeChatStreamAPI_1122.class);
    private final ConcurrentHashMap<String, Set<StreamObserver<ServerStreamResponse>>> chatStreams = new ConcurrentHashMap<>();

    /**
     * Registers a new client stream to a chat room.
     * @param chatId The ID of the chat room to join.
     * @param responseObserver The client's response stream observer.
     */
    public void register(String chatId, StreamObserver<ServerStreamResponse> responseObserver) {
        long startTime = System.currentTimeMillis();
        chatStreams.computeIfAbsent(chatId, k -> ConcurrentHashMap.newKeySet()).add(responseObserver);
        logger.info("Registered new stream for chatID: {}. Total streams for chat: {}. Time: {}ms", chatId, chatStreams.get(chatId).size(), (System.currentTimeMillis() - startTime));
    }

    /**
     * Removes a client stream from a chat room.
     * @param chatId The ID of the chat room to leave.
     * @param responseObserver The client's response stream observer.
     */
    public void deregister(String chatId, StreamObserver<ServerStreamResponse> responseObserver) {
        long startTime = System.currentTimeMillis();
        if (chatId == null || chatId.isEmpty()) {
            logger.warn("Attempted to deregister a stream with null or empty chatId. Time: {}ms", (System.currentTimeMillis() - startTime));
            return;
        }
        Set<StreamObserver<ServerStreamResponse>> observers = chatStreams.get(chatId);
        if (observers != null) {
            observers.remove(responseObserver);
            if (observers.isEmpty()) {
                chatStreams.remove(chatId);
            }
            logger.info("Deregistered stream from chatID: {}. Total streams for chat: {}. Time: {}ms", chatId, observers.size(), (System.currentTimeMillis() - startTime));
        } else {
             logger.warn("Attempted to deregister from a non-existent chat stream set for chatID: {}. Time: {}ms", chatId, (System.currentTimeMillis() - startTime));
        }
    }

    /**
     * Broadcasts a response to all clients in a specific chat room.
     * @param chatId The ID of the target chat room.
     * @param response The response to send.
     */
    public void broadcast(String chatId, ServerStreamResponse response) {
        long startTime = System.currentTimeMillis();
        Set<StreamObserver<ServerStreamResponse>> observers = chatStreams.getOrDefault(chatId, Collections.emptySet());
        logger.debug("Broadcasting to {} observers in chat {}", observers.size(), chatId);
        for (StreamObserver<ServerStreamResponse> observer : observers) {
            try {
                observer.onNext(response);
            } catch (Exception e) {
                logger.error("Failed to send message to an observer for chat {}. Error: {}. Removing observer.", chatId, e.getMessage());
                // Consider removing the dead observer here.
            }
        }
        logger.info("Broadcast to chatID: {} completed. Time: {}ms", chatId, (System.currentTimeMillis() - startTime));
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/util/GrpcErrorUtil_RealTimeChatStreamAPI_1122.java