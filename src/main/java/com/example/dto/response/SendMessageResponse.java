package com.example.dto.response;

import com.example.model.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for the response after sending a message.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageResponse {
    private UUID messageId;
    private MessageStatus status;
}