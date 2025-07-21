package com.yourorg.fetchconversationsapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO representing a single conversation.
 */
@Data
@Builder
public class ConversationFCA8123 {
    private String id;
    private String title;
    private String avatarUrl;
    private List<ParticipantFCA8123> participants;
    private LastMessageFCA8123 lastMessage;
    private int unreadCount;
    private boolean isMuted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createdAt;
}
```
src/main/java/com/yourorg/fetchconversationsapi/dto/PaginationInfoFCA8123.java
```java