package com.fetchmessagesapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for pagination information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationInfoFMA1 {
    @JsonProperty("next_cursor")
    private String nextCursor;

    @JsonProperty("has_more")
    private boolean hasMore;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/dto/SenderDtoFMA1.java