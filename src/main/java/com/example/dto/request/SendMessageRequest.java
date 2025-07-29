package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for sending a new message.
 */
@Data
public class SendMessageRequest {
    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;

    @NotBlank(message = "Message content cannot be empty")
    @Size(max = 5000, message = "Message content cannot exceed 5000 characters")
    private String content;
}