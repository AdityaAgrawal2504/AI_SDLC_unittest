package com.yourorg.fetchmessagesapi.model.dto.fma1;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Root DTO for the API response, containing message data and pagination info.
 */
@Data
@Builder
public class MessageListResponseFMA_1 {
    private List<MessageDtoFMA_1> data;
    private PaginationInfoFMA_1 pagination;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/entity/fma1/MessageFMA_1.java