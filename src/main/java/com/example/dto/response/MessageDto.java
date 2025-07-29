package com.example.dto.response;

import com.example.model.MessageStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing a single message.
 */
@Data
@Builder
public class MessageDto {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private Instant sentAt;
    private MessageStatus status;
}