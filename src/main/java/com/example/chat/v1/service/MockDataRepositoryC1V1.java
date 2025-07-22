package com.example.chat.v1.service;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.grpc.MessageStatus;
import com.example.chat.v1.util.IdGeneratorC1V1;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Mock repository to simulate database interactions with in-memory data.
 */
@Repository
public class MockDataRepositoryC1V1 {

    private final IdGeneratorC1V1 idGenerator;
    private final Map<String, UserC1V1> users = new ConcurrentHashMap<>();
    private final Map<String, List<String>> conversationMembers = new ConcurrentHashMap<>();
    private final Map<String, MessageC1V1> messages = new ConcurrentHashMap<>();
    private final Map<String, String> clientMessageIdIndex = new ConcurrentHashMap<>();

    public MockDataRepositoryC1V1(IdGeneratorC1V1 idGenerator) {
        this.idGenerator = idGenerator;
    }

    @PostConstruct
    public void init() {
        // Create mock users
        UserC1V1 user1 = new UserC1V1(idGenerator.newUserId(), "Alice", "http://example.com/avatar/alice.png");
        UserC1V1 user2 = new UserC1V1(idGenerator.newUserId(), "Bob", "http://example.com/avatar/bob.png");
        UserC1V1 user3 = new UserC1V1(idGenerator.newUserId(), "Charlie", "http://example.com/avatar/charlie.png");
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);

        // Create a mock conversation
        String convId = idGenerator.newConversationId();
        conversationMembers.put(convId, Arrays.asList(user1.getId(), user2.getId()));

        String convId2 = idGenerator.newConversationId();
        conversationMembers.put(convId2, Arrays.asList(user1.getId(), user3.getId()));

        // Add some initial messages
        MessageC1V1 initialMessage1 = MessageC1V1.builder()
                .id(idGenerator.newMessageId())
                .clientMessageId(UUID.randomUUID().toString())
                .conversationId(convId)
                .senderId(user1.getId())
                .content("Hello Bob!")
                .createdAt(Instant.now().minusSeconds(3600))
                .status(MessageStatus.SENT)
                .build();
        saveMessage(initialMessage1);

        MessageC1V1 initialMessage2 = MessageC1V1.builder()
                .id(idGenerator.newMessageId())
                .clientMessageId(UUID.randomUUID().toString())
                .conversationId(convId)
                .senderId(user2.getId())
                .content("Hi Alice!")
                .createdAt(Instant.now().minusSeconds(3500))
                .status(MessageStatus.SENT)
                .build();
        saveMessage(initialMessage2);
    }

    public Optional<UserC1V1> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public Optional<MessageC1V1> findMessageByClientMessageId(String clientMessageId) {
        String messageId = clientMessageIdIndex.get(clientMessageId);
        if (messageId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(messages.get(messageId));
    }
    
    public Optional<MessageC1V1> findMessageById(String messageId) {
        return Optional.ofNullable(messages.get(messageId));
    }

    public boolean isUserMemberOfConversation(String userId, String conversationId) {
        List<String> members = conversationMembers.get(conversationId);
        return members != null && members.contains(userId);
    }

    public boolean conversationExists(String conversationId) {
        return conversationMembers.containsKey(conversationId);
    }
    
    public MessageC1V1 saveMessage(MessageC1V1 message) {
        messages.put(message.getId(), message);
        clientMessageIdIndex.put(message.getClientMessageId(), message.getId());
        return message;
    }

    public List<String> getConversationMemberIds(String conversationId) {
        return conversationMembers.getOrDefault(conversationId, Collections.emptyList());
    }

    public Collection<UserC1V1> getAllUsers() {
        return users.values();
    }
     
    public Collection<String> getAllConversationIds() {
        return conversationMembers.keySet();
    }
}
```
```java
// src/main/java/com/example/chat/v1/service/AuthenticationServiceC1V1.java