package com.yourorg.fetchmessagesapi.exception.fma1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Standard error response body for API exceptions.
 */
@Data
@AllArgsConstructor
public class ErrorResponseFMA_1 {
    @JsonProperty("error_code")
    private String errorCode;

    private String message;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/exception/fma1/CustomApiExceptionFMA_1.java