package com.yourorg.fetchmessagesapi.model.dto.fma1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yourorg.fetchmessagesapi.model.enums.fma1.MessageContentTypeFMA_1;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for the content of a message.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageContentDtoFMA_1 {
    private MessageContentTypeFMA_1 type;
    private String text;
    private String url;
    private MessageMetadataDtoFMA_1 metadata;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/dto/fma1/SenderDtoFMA_1.java