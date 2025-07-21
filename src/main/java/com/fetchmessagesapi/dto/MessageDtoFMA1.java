package com.fetchmessagesapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing a single message.
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class MessageDtoFMA1 {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("conversation_id")
    private UUID conversationId;

    @JsonProperty("sender")
    private SenderDtoFMA1 sender;

    @JsonProperty("content")
    private MessageContentDtoFMA1 content;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/dto/MessageListResponseFMA1.java