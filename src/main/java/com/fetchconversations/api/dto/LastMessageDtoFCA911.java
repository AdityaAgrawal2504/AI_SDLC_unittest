package com.fetchconversations.api.dto;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class LastMessageDtoFCA911 {
    private UUID id;
    private String contentSnippet;
    private UUID senderId;
    private OffsetDateTime timestamp;
    private boolean isSeen;
}
```
```java
// src/main/java/com/fetchconversations/api/dto/PaginatedConversationsResponseFCA911.java