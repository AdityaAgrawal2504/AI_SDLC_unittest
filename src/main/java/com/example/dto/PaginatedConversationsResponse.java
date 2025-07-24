package com.example.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaginatedConversationsResponse extends PaginatedResponse<ConversationResponse> {
    public PaginatedConversationsResponse(List<ConversationResponse> items, long total, int page, int limit) {
        super(items, total, page, limit);
    }
}