package com.verifyotpapi.dto.response;

import com.verifyotpapi.util.ErrorCode_VOTP1;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a standardized error response body for API failures.
 */
@Data
@AllArgsConstructor
public class ApiErrorResponse_VOTP1 {
    private String errorCode;
    private String errorMessage;

    /**
     * Creates an ApiErrorResponse from an ErrorCode enum.
     */
    public ApiErrorResponse_VOTP1(ErrorCode_VOTP1 errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }
}
```
```java
// src/main/java/com/verifyotpapi/exception/ApiException_VOTP1.java