package com.yourorg.fetchmessagesapi.model.dto.fma1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO representing a single message within a conversation.
 */
@Data
@Builder
public class MessageDtoFMA_1 {
    private UUID id;

    @JsonProperty("conversation_id")
    private UUID conversationId;

    private SenderDtoFMA_1 sender;
    private MessageContentDtoFMA_1 content;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/dto/fma1/PaginationInfoFMA_1.java