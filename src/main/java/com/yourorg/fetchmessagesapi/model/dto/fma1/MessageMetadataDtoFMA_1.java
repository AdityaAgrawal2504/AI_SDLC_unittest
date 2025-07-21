package com.yourorg.fetchmessagesapi.model.dto.fma1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for additional metadata about message content.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageMetadataDtoFMA_1 {
    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_size")
    private Integer fileSize;

    @JsonProperty("mime_type")
    private String mimeType;

    private Integer width;
    private Integer height;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/dto/fma1/MessageContentDtoFMA_1.java