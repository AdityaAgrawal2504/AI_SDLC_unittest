package com.example.conversation.history.fch01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for the paginated response for a message history request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedMessagesResponseFCHAPI01 {
    private List<MessageDtoFCHAPI01> data;
    private PaginationInfoFCHAPI01 pagination;
}
src/main/java/com/example/conversation/history/fch01/dto/PaginationInfoFCHAPI01.java