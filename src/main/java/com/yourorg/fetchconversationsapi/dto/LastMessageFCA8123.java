package com.yourorg.fetchconversationsapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

/**
 * DTO for the last message in a conversation.
 */
@Data
@Builder
public class LastMessageFCA8123 {
    private String id;
    private String contentSnippet;
    private String senderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime timestamp;
    private boolean isSeen;
}
```
src/main/java/com/yourorg/fetchconversationsapi/dto/ParticipantFCA8123.java
```java