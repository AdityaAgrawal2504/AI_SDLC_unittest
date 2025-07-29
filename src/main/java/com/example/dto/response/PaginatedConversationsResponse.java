package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for a paginated list of conversation summaries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedConversationsResponse {
    private List<ConversationSummary> items;
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}