package com.example.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for a standardized error response.
 * Provides consistent error details for API clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseLCVAPI_1 {

    /**
     * A machine-readable code for the specific error.
     */
    private String errorCode;

    /**
     * A human-readable description of the error.
     */
    private String errorMessage;

    /**
     * The ISO 8601 timestamp when the error occurred.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private ZonedDateTime timestamp;

    /**
     * The unique identifier for the API request that failed.
     */
    private String requestId;
}
```

src/main/java/com/example/api/auth/controller/AuthenticationControllerLCVAPI_1.java
```java