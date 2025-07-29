package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedConversationResponseDto {
    private List<ConversationDto> data;
    private PaginationInfo pagination;
}