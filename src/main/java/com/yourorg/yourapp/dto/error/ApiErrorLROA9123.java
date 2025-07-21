package com.yourorg.yourapp.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Standard structure for API error responses.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorLROA9123 {

    private final String errorCode;
    private final String errorMessage;
    private List<ValidationErrorDetailLROA9123> errorDetails;

}
```
src/main/java/com/yourorg/yourapp/dto/error/ValidationErrorDetailLROA9123.java
```java