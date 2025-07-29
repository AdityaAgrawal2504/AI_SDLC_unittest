package com.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String phoneNumber;
    private String name;
    private LocalDateTime createdAt;
}