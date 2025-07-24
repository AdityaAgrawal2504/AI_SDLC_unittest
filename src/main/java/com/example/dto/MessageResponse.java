package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private UUID id;
    @JsonProperty("conversation_id")
    private UUID conversationId;
    @JsonProperty("sender_id")
    private UUID senderId;
    private String content;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
}