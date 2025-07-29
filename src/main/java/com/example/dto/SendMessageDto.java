package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.UUID;

@Data
public class SendMessageDto {

    @NotNull(message = "Recipient ID is required.")
    private UUID recipientId;

    @NotBlank(message = "Content is required.")
    @Size(max = 5000, message = "Content must not exceed 5000 characters.")
    private String content;
}