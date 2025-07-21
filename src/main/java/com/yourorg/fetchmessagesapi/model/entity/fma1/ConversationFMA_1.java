package com.yourorg.fetchmessagesapi.model.entity.fma1;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

/**
 * Represents a Conversation entity, used for authorization checks.
 */
@Data
@Builder
public class ConversationFMA_1 {
    private UUID id;
    private Set<UUID> participantIds;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/entity/fma1/UserFMA_1.java