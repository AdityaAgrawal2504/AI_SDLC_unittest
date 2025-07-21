package com.mycompany.chatapp.service;

import com.mycompany.chatapp.util.IdempotencyManager_RealTimeChatStreamAPI_1122;
import com.mycompany.chatapp.util.SanitizationUtil_RealTimeChatStreamAPI_1122;
import com.mycompany.chatapp.grpc.NewMessageEvent;
import org.springframework.stereotype.Service;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

import java.time.Instant;
import java.util.UUID;

/**
 * Service responsible for processing, sanitizing, and preparing new messages.
 */
@Service
public class MessageService_RealTimeChatStreamAPI_1122 {

    private final IdempotencyManager_RealTimeChatStreamAPI_1122 idempotencyManager;

    public MessageService_RealTimeChatStreamAPI_1122(IdempotencyManager_RealTimeChatStreamAPI_1122 idempotencyManager) {
        this.idempotencyManager = idempotencyManager;
    }

    /**
     * Processes and creates a new message event.
     * @param chatId The chat ID.
     * @param content The raw message content.
     * @param senderId The sender's user ID.
     * @param senderDisplayName The sender's display name.
     * @param parentMessageId Optional ID of the message being replied to.
     * @return A NewMessageEvent ready to be broadcast.
     */
    public NewMessageEvent createNewMessage(String chatId, String content, String senderId, String senderDisplayName, String parentMessageId) {
        String sanitizedContent = SanitizationUtil_RealTimeChatStreamAPI_1122.sanitize(content);
        Timestamp now = Timestamps.fromMillis(Instant.now().toEpochMilli());

        NewMessageEvent.Builder eventBuilder = NewMessageEvent.newBuilder()
                .setMessageId(UUID.randomUUID().toString())
                .setChatId(chatId)
                .setSenderId(senderId)
                .setSenderDisplayName(senderDisplayName)
                .setContent(sanitizedContent)
                .setTimestamp(now);

        if (parentMessageId != null && !parentMessageId.isEmpty()) {
            eventBuilder.setParentMessageId(parentMessageId);
        }

        // In a real app, this is where you'd persist the message to a database.
        return eventBuilder.build();
    }

    /**
     * Checks if an idempotency key has been processed before.
     * @param key The idempotency key.
     * @return true if it's a new key, false if it's a duplicate.
     */
    public boolean claimIdempotencyKey(String key) {
        return idempotencyManager.claim(key);
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/service/ChatStreamManager_RealTimeChatStreamAPI_1122.java