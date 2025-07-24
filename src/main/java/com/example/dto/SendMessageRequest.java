package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.UUID;

@Data
public class SendMessageRequest {
    @JsonProperty("recipient_id")
    @NotNull(message = "Recipient ID is required.")
    private UUID recipientId;

    @NotBlank(message = "Content is required.")
    @Size(max = 5000, message = "Content length cannot exceed 5000 characters.")
    private String content;
}