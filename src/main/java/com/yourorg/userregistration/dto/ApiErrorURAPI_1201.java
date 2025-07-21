package com.yourorg.userregistration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

/**
 * DTO for standardized API error responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorURAPI_1201 {

    private String errorCode;
    private String message;
    private Map<String, String> details;

    public ApiErrorURAPI_1201(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ApiErrorURAPI_1201(String errorCode, String message, Map<String, String> details) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }

    // Getters and Setters
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/enums/ErrorCodeURAPI_1201.java