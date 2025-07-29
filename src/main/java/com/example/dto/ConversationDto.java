src/main/java/com/example/dto/ConversationDto.java
package com.example.dto;

import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Public representation of a conversation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDto {
    private UUID id;
    private List<UserDto> participants;
    private MessageDto lastMessage;
    private Integer unreadCount;
    private OffsetDateTime updatedAt;

    /**
     * Maps a Conversation entity to a ConversationDto.
     * @param conversation The Conversation entity.
     * @param participants The set of User entities in the conversation.
     * @param lastMessage The last message entity.
     * @param unreadCount The count of unread messages for the current user.
     * @return A ConversationDto instance.
     */
    public static ConversationDto fromEntity(Conversation conversation, Set<User> participants, Message lastMessage, Integer unreadCount) {
        ConversationDto dto = new ConversationDto();
        dto.setId(conversation.getId());
        dto.setUpdatedAt(conversation.getUpdatedAt());
        dto.setParticipants(participants.stream().map(UserDto::fromEntity).collect(Collectors.toList()));
        if (lastMessage != null) {
            dto.setLastMessage(MessageDto.fromEntity(lastMessage));
        }
        dto.setUnreadCount(unreadCount);
        return dto;
    }
}