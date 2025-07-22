package com.example.conversation.history.fch01.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Represents the standard structure for an API error response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorFCHAPI01 {
    private String errorCode;
    private String message;
    private List<ValidationErrorDetailFCHAPI01> details;
}
src/main/java/com/example/conversation/history/fch01/dto/MessageDtoFCHAPI01.java