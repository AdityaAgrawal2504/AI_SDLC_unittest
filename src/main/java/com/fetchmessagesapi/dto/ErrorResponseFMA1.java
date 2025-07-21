package com.fetchmessagesapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for standardized error responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseFMA1 {
    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("message")
    private String message;

    public ErrorResponseFMA1(ErrorCodeFMA1 code, String message) {
        this.errorCode = code.getCode();
        this.message = message;
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/entity/ConversationFMA1.java