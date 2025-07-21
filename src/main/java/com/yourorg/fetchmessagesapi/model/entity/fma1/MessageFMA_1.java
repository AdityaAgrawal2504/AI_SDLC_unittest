package com.yourorg.fetchmessagesapi.model.entity.fma1;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Represents a Message entity, as it would be stored in a database.
 */
@Data
@Builder
public class MessageFMA_1 {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String contentJson; // Storing complex content as JSON in DB
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/entity/fma1/ConversationFMA_1.java