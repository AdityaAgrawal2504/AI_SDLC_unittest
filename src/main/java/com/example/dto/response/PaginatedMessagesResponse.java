package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for a paginated list of messages.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedMessagesResponse {
    private List<MessageDto> items;
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}