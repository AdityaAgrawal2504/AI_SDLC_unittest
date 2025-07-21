package com.fetchconversations.api.dto;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ConversationDtoFCA911 {
    private UUID id;
    private String title;
    private String avatarUrl;
    private List<ParticipantDtoFCA911> participants;
    private LastMessageDtoFCA911 lastMessage;
    private int unreadCount;
    private boolean isMuted;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
}
```
```java
// src/main/java/com/fetchconversations/api/dto/LastMessageDtoFCA911.java