package com.yourorg.fetchconversationsapi.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * DTO for the top-level paginated response for conversations.
 */
@Data
@Builder
public class PaginatedConversationsResponseFCA8123 {
    private List<ConversationFCA8123> data;
    private PaginationInfoFCA8123 pagination;
}
```
src/main/java/com/yourorg/fetchconversationsapi/exception/GlobalExceptionHandlerFCA8123.java
```java