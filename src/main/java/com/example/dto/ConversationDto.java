package com.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ConversationDto {
    private UUID id;
    private List<UserResponseDto> participants;
    private MessageDto lastMessage;
    private long unreadCount;
    private LocalDateTime updatedAt;
}