package com.mycompany.chatapp.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock service for managing chat rooms and memberships.
 */
@Service
public class ChatRoomService_RealTimeChatStreamAPI_1122 {

    private final Set<String> existingChatRooms = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<String, Set<String>> chatMembers = new ConcurrentHashMap<>();

    public ChatRoomService_RealTimeChatStreamAPI_1122() {
        // Pre-populate with some data for testing
        String chat1 = "a1b2c3d4-e5f6-7890-1234-567890abcdef";
        String chat2 = "f0e9d8c7-b6a5-4321-fedc-ba0987654321";
        existingChatRooms.add(chat1);
        existingChatRooms.add(chat2);

        // User from a valid token "valid-token-user1"
        String user1 = "3325c8d3-5240-3b47-8a62-7935406c8842";
        // User from a valid token "valid-token-user2"
        String user2 = "09440b2e-212d-3467-9d7a-77405e4663e0";
        chatMembers.put(chat1, ConcurrentHashMap.newKeySet());
        chatMembers.get(chat1).add(user1);
        chatMembers.get(chat1).add(user2);
    }


    /**
     * Checks if a chat room with the given ID exists.
     * @param chatId The UUID of the chat room.
     * @return true if the chat room exists, false otherwise.
     */
    public boolean chatExists(String chatId) {
        return existingChatRooms.contains(chatId);
    }

    /**
     * Checks if a user is a member of a specific chat room.
     * @param userId The ID of the user.
     * @param chatId The ID of the chat room.
     * @return true if the user is a member, false otherwise.
     */
    public boolean isUserMemberOfChat(String userId, String chatId) {
        return chatMembers.containsKey(chatId) && chatMembers.get(chatId).contains(userId);
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/service/MessageService_RealTimeChatStreamAPI_1122.java