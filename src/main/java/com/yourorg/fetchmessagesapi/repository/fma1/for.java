package com.yourorg.fetchmessagesapi.repository.fma1;

import com.yourorg.fetchmessagesapi.model.entity.fma1.ConversationFMA_1;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing conversation data.
 */
public interface ConversationRepositoryFMA_1 {
    Optional<ConversationFMA_1> findById(UUID conversationId);
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/repository/fma1/MessageRepositoryFMA_1.java