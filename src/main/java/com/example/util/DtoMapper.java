package com.example.util;

import com.example.dto.response.*;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;

/**
 * A utility class for mapping between domain models (entities) and Data Transfer Objects (DTOs).
 */
public class DtoMapper {

    /**
     * Maps a User entity to a UserSignupResponse DTO.
     * @param user The User entity.
     * @return The corresponding UserSignupResponse DTO.
     */
    public static UserSignupResponse toUserSignupResponse(User user) {
        return UserSignupResponse.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }
    
    /**
     * Maps a Message entity to a MessageDto.
     * @param message The Message entity.
     * @return The corresponding MessageDto.
     */
    public static MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .status(message.getStatus())
                .build();
    }
    
    /**
     * Maps a Conversation and related info to a ConversationSummary DTO.
     * @param conversation The Conversation entity.
     * @param otherParticipant The other user in the conversation.
     * @param lastMessage The last message sent in the conversation.
     * @param unreadCount The count of unread messages for the current user.
     * @return The corresponding ConversationSummary DTO.
     */
    public static ConversationSummary toConversationSummary(Conversation conversation, User otherParticipant, Message lastMessage, long unreadCount) {
        return ConversationSummary.builder()
                .id(conversation.getId())
                .participant(otherParticipant != null ? ParticipantDto.builder()
                    .id(otherParticipant.getId())
                    .name(otherParticipant.getName())
                    .build() : null)
                .lastMessage(lastMessage != null ? LastMessageDto.builder()
                    .content(lastMessage.getContent())
                    .sentAt(lastMessage.getSentAt())
                    .build() : null)
                .unreadCount(unreadCount)
                .updatedAt(conversation.getUpdatedAt())
                .build();
    }
}