package com.fetchconversations.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedConversationsResponseFCA911 {
    private List<ConversationDtoFCA911> data;
    private PaginationInfoFCA911 pagination;
}
```
```java
// src/main/java/com/fetchconversations/api/dto/PaginationInfoFCA911.java