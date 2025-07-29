package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class MarkAsReadDto {

    @NotNull(message = "Last read timestamp is required.")
    private OffsetDateTime lastReadTimestamp;
}