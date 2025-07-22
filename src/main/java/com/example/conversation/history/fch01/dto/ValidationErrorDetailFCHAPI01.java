package com.example.conversation.history.fch01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO providing specific detail about a single validation error.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDetailFCHAPI01 {
    private String field;
    private String issue;
}
src/main/java/com/example/conversation/history/fch01/enums/ErrorCodeFCHAPI01.java