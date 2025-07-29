package com.example.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

/**
 * DTO representing a participant in a conversation summary.
 */
@Data
@Builder
public class ParticipantDto {
    private UUID id;
    private String name;
}