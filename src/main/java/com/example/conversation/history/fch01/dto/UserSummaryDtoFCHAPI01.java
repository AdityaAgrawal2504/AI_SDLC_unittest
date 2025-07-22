package com.example.conversation.history.fch01.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

/**
 * DTO representing a summary of a user.
 */
@Data
@Builder
public class UserSummaryDtoFCHAPI01 {
    private UUID id;
    private String displayName;
    private String avatarUrl;
}
src/main/java/com/example/conversation/history/fch01/dto/ValidationErrorDetailFCHAPI01.java