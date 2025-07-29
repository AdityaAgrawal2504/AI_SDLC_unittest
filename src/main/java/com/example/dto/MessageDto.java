package com.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDto {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private LocalDateTime createdAt;
}