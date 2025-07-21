package com.yourorg.fetchmessagesapi.model.dto.fma1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for pagination metadata.
 */
@Data
@Builder
public class PaginationInfoFMA_1 {
    @JsonProperty("next_cursor")
    private String nextCursor;

    @JsonProperty("has_more")
    private boolean hasMore;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/dto/fma1/MessageListResponseFMA_1.java