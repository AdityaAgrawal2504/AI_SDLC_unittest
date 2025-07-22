package com.example.conversation.history.fch01.dto;

import com.example.conversation.history.fch01.enums.MessageTypeFCHAPI01;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for a single message within a conversation.
 */
@Data
@Builder
public class MessageDtoFCHAPI01 {
    private UUID id;
    private MessageTypeFCHAPI01 type;
    private String content;
    private UserSummaryDtoFCHAPI01 sender;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Map<String, Object> metadata;
}
src/main/java/com/example/conversation/history/fch01/dto/PaginatedMessagesResponseFCHAPI01.java