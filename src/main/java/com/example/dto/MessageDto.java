src/main/java/com/example/dto/MessageDto.java
package com.example.dto;

import com.example.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Public representation of a message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private OffsetDateTime createdAt;

    /**
     * Maps a Message entity to a MessageDto.
     * @param message The Message entity.
     * @return A MessageDto instance.
     */
    public static MessageDto fromEntity(Message message) {
        return new MessageDto(
                message.getId(),
                message.getConversationId(),
                message.getSenderId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}