package com.fetchmessagesapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for metadata associated with message content.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataDtoFMA1 {
    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_size")
    private Long fileSize;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/dto/PaginationInfoFMA1.java