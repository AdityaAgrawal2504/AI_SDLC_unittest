package com.fetchmessagesapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fetchmessagesapi.enums.ContentTypeFMA1;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for message content.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageContentDtoFMA1 {
    @JsonProperty("type")
    private ContentTypeFMA1 type;

    @JsonProperty("text")
    private String text;

    @JsonProperty("url")
    private String url;

    @JsonProperty("metadata")
    private MetadataDtoFMA1 metadata;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/dto/MessageDtoFMA1.java