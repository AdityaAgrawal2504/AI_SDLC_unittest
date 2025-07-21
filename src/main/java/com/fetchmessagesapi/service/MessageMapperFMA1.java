package com.fetchmessagesapi.service;

import com.fetchmessagesapi.dto.MessageContentDtoFMA1;
import com.fetchmessagesapi.dto.MessageDtoFMA1;
import com.fetchmessagesapi.dto.MetadataDtoFMA1;
import com.fetchmessagesapi.dto.SenderDtoFMA1;
import com.fetchmessagesapi.entity.MessageFMA1;
import org.springframework.stereotype.Component;

/**
 * Maps Message entities to Message DTOs.
 */
@Component
public class MessageMapperFMA1 {

    /**
     * Converts a Message entity to a Message DTO.
     * @param entity The MessageFMA1 entity.
     * @return The corresponding MessageDtoFMA1.
     */
    public MessageDtoFMA1 toDto(MessageFMA1 entity) {
        if (entity == null) {
            return null;
        }
        return MessageDtoFMA1.builder()
                .id(entity.getId())
                .conversationId(entity.getConversationId())
                .sender(buildSenderDto(entity))
                .content(buildContentDto(entity))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private SenderDtoFMA1 buildSenderDto(MessageFMA1 entity) {
        return SenderDtoFMA1.builder()
                .id(entity.getSenderId())
                .type(entity.getSenderType())
                .displayName(entity.getSenderDisplayName())
                .build();
    }

    private MessageContentDtoFMA1 buildContentDto(MessageFMA1 entity) {
        return MessageContentDtoFMA1.builder()
                .type(entity.getContentType())
                .text(entity.getTextContent())
                .url(entity.getUrl())
                .metadata(buildMetadataDto(entity))
                .build();
    }

    private MetadataDtoFMA1 buildMetadataDto(MessageFMA1 entity) {
        if (entity.getMetadataFileName() == null && entity.getMetadataFileSize() == null && entity.getMetadataMimeType() == null &&
            entity.getMetadataWidth() == null && entity.getMetadataHeight() == null) {
            return null;
        }
        return MetadataDtoFMA1.builder()
                .fileName(entity.getMetadataFileName())
                .fileSize(entity.getMetadataFileSize())
                .mimeType(entity.getMetadataMimeType())
                .width(entity.getMetadataWidth())
                .height(entity.getMetadataHeight())
                .build();
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/service/MessageServiceFMA1.java