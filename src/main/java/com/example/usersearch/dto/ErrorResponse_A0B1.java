package com.example.usersearch.dto;

import com.example.usersearch.enums.ErrorCode_A0B1;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse_A0B1 {
    private final String errorCode;
    private final String message;
    private Map<String, Object> details;

    public ErrorResponse_A0B1(ErrorCode_A0B1 errorCode, Map<String, Object> details) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.details = details;
    }

    public ErrorResponse_A0B1(ErrorCode_A0B1 errorCode) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
```
src/main/java/com/example/usersearch/enums/ErrorCode_A0B1.java
```java