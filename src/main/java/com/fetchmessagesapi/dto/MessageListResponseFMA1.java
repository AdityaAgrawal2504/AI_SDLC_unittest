package com.fetchmessagesapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Root DTO for the API response, containing message data and pagination info.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageListResponseFMA1 {
    @JsonProperty("data")
    private List<MessageDtoFMA1> data;

    @JsonProperty("pagination")
    private PaginationInfoFMA1 pagination;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/dto/MetadataDtoFMA1.java