package com.example.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * DTO representing a standardized error response for all API failures.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse_17169 {

    private String errorCode;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private ZonedDateTime timestamp;
}
src/main/java/com/example/auth/enums/ErrorCode_AuthVerifyOtp_17169.java