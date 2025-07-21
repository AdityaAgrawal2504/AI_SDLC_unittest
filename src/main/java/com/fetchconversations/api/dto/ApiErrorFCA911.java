package com.fetchconversations.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorFCA911 {
    private int statusCode;
    private String errorCode;
    private String message;
    private Map<String, String> details;
}
```
```java
// src/main/java/com/fetchconversations/api/dto/ConversationDtoFCA911.java