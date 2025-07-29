package com.example.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing a summary of a single conversation for list views.
 */
@Data
@Builder
public class ConversationSummary {
    private UUID id;
    private ParticipantDto participant;
    private LastMessageDto lastMessage;
    private long unreadCount;
    private Instant updatedAt;
}