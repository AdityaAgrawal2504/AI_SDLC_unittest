src/main/java/com/example/exception/ErrorResponse.java
package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for error responses, matching the API specification.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
}