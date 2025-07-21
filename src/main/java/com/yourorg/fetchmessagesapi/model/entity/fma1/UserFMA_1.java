package com.yourorg.fetchmessagesapi.model.entity.fma1;

import com.yourorg.fetchmessagesapi.model.enums.fma1.SenderTypeFMA_1;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Represents a User/Sender entity, to be retrieved based on sender ID.
 */
@Data
@Builder
public class UserFMA_1 {
    private UUID id;
    private SenderTypeFMA_1 type;
    private String displayName;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/exception/fma1/ErrorResponseFMA_1.java