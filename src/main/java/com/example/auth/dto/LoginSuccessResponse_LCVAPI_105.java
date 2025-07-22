package com.example.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for a successful login and OTP dispatch response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessResponse_LCVAPI_105 {

    /**
     * A confirmation message indicating the OTP has been dispatched.
     */
    private String message;

    /**
     * A unique identifier for the API request for tracking and logging.
     */
    private String requestId;
}
src/main/java/com/example/auth/enums/ErrorCode_LCVAPI_107.java