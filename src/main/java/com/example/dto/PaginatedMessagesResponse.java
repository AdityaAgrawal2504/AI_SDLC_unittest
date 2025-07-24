package com.example.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaginatedMessagesResponse extends PaginatedResponse<MessageResponse> {
    public PaginatedMessagesResponse(List<MessageResponse> items, long total, int page, int limit) {
        super(items, total, page, limit);
    }
}