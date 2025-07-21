package com.fetchconversations.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfoFCA911 {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
```
```java
// src/main/java/com/fetchconversations/api/dto/ParticipantDtoFCA911.java