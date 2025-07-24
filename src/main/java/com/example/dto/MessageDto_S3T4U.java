src/main/java/com/example/dto/MessageDto_S3T4U.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto_S3T4U {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private LocalDateTime createdAt;
}