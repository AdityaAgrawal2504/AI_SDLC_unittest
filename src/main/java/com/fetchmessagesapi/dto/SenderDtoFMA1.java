package com.fetchmessagesapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fetchmessagesapi.enums.SenderTypeFMA1;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.UUID;

/**
 * DTO for the sender of a message.
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class SenderDtoFMA1 {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("type")
    private SenderTypeFMA1 type;

    @JsonProperty("display_name")
    private String displayName;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/dto/ErrorResponseFMA1.java