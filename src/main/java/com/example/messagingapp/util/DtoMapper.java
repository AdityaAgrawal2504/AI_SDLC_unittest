package com.example.messagingapp.util;

import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.dto.UserResponse;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;

public final class DtoMapper {

    private DtoMapper() {}

    /**
     * Converts a User entity to a UserResponse DTO.
     * @param user The User entity.
     * @return The corresponding UserResponse DTO.
     */
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * Converts a Message entity to a MessageResponse DTO.
     * @param message The Message entity.
     * @return The corresponding MessageResponse DTO.
     */
    public static MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .recipientId(message.getRecipient().getId())
                .content(message.getContent())
                .status(message.getStatus())
                .createdAt(message.getCreatedAt())
                .build();
    }
}