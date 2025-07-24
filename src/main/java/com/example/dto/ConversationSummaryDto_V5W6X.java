src/main/java/com/example/dto/ConversationSummaryDto_V5W6X.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummaryDto_V5W6X {
    private UUID id;
    private List<UserDto_D3E4F> participants;
    private MessageDto_S3T4U lastMessage;
    private long unreadCount;
    private LocalDateTime updatedAt;
}