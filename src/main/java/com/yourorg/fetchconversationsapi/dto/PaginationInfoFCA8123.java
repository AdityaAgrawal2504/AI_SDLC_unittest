package com.yourorg.fetchconversationsapi.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for pagination metadata.
 */
@Data
@Builder
public class PaginationInfoFCA8123 {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
```
src/main/java/com/yourorg/fetchconversationsapi/dto/PaginatedConversationsResponseFCA8123.java
```java