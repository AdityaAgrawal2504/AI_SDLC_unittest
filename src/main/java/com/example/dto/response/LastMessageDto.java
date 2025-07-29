package com.example.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

/**
 * DTO representing the last message in a conversation summary.
 */
@Data
@Builder
public class LastMessageDto {
    private String content;
    private Instant sentAt;
}