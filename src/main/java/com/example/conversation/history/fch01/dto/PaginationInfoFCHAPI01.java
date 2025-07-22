package com.example.conversation.history.fch01.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

/**
 * DTO containing metadata for pagination.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationInfoFCHAPI01 {
    private OffsetDateTime nextCursor;
    private boolean hasMore;
}
src/main/java/com/example/conversation/history/fch01/dto/UserSummaryDtoFCHAPI01.java