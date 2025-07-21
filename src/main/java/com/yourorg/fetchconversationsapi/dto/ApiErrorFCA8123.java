package com.yourorg.fetchconversationsapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Represents a standardized API error response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorFCA8123 {
    private int statusCode;
    private String errorCode;
    private String message;
    private Map<String, String> details;
}
```
src/main/java/com/yourorg/fetchconversationsapi/dto/LastMessageFCA8123.java
```java