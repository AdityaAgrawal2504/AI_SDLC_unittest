package com.yourorg.fetchmessagesapi.model.dto.fma1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yourorg.fetchmessagesapi.model.enums.fma1.SenderTypeFMA_1;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for the sender of a message.
 */
@Data
@Builder
public class SenderDtoFMA_1 {
    private UUID id;
    private SenderTypeFMA_1 type;

    @JsonProperty("display_name")
    private String displayName;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/dto/fma1/MessageDtoFMA_1.java