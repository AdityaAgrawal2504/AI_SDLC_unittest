package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private UUID id;
    private List<UserResponse> participants;
    @JsonProperty("last_message")
    private MessageResponse lastMessage;
    @JsonProperty("unread_count")
    private long unreadCount;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}