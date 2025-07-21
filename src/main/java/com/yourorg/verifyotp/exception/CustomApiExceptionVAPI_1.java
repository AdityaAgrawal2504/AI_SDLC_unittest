package com.yourorg.verifyotp.exception;

import com.yourorg.verifyotp.constants.ErrorCodeVAPI_1;
import org.springframework.http.HttpStatus;

public class CustomApiExceptionVAPI_1 extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public CustomApiExceptionVAPI_1(ErrorCodeVAPI_1 errorCode) {
        super(errorCode.getErrorMessage());
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.name();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```
```java
// Exception Handler: GlobalExceptionHandlerVAPI_1.java