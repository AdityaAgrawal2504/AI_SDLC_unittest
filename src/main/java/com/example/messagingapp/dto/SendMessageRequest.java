package com.example.messagingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    @NotNull(message = "Recipient ID is required.")
    private UUID recipientId;

    @NotBlank(message = "Content is required.")
    @Size(max = 5000, message = "Message content cannot exceed 5000 characters.")
    private String content;
}