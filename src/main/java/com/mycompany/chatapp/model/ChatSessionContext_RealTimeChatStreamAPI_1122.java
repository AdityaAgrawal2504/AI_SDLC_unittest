package com.mycompany.chatapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Holds the state for a single connected client stream.
 */
@Getter
@Setter
public class ChatSessionContext_RealTimeChatStreamAPI_1122 {
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private String userId;
    private String chatId;
    private String displayName;

    /**
     * Checks if the initial connection has been successfully established.
     * @return true if connected, false otherwise.
     */
    public boolean isConnected() {
        return connected.get();
    }

    /**
     * Marks the session as connected and stores user details.
     * @param user The authenticated user.
     * @param chatId The ID of the chat room.
     */
    public void establishConnection(AuthenticatedUser_RealTimeChatStreamAPI_1122 user, String chatId) {
        this.userId = user.getUserId();
        this.displayName = user.getDisplayName();
        this.chatId = chatId;
        this.connected.set(true);
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/service/AuthenticationService_RealTimeChatStreamAPI_1122.java