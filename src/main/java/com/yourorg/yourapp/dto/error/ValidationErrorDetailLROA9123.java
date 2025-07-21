package com.yourorg.yourapp.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for providing specific details about a validation error.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDetailLROA9123 {
    private String field;
    private String issue;
}
```
src/main/java/com/yourorg/yourapp/model/UserLROA9123.java
```java