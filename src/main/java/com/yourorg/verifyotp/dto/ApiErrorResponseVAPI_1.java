package com.yourorg.verifyotp.dto;

public class ApiErrorResponseVAPI_1 {

    private String errorCode;
    private String errorMessage;
    
    public ApiErrorResponseVAPI_1(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
```
```java
// Model: OtpDataVAPI_1.java