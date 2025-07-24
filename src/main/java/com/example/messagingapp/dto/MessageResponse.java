package com.example.messagingapp.dto;

import com.example.messagingapp.model.MessageStatus;
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
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private MessageStatus status;
    private OffsetDateTime createdAt;
}