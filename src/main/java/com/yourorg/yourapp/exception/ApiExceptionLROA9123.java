package com.yourorg.yourapp.exception;

import com.yourorg.yourapp.enums.ErrorCodeLROA9123;
import lombok.Getter;

/**
 * Base custom exception for handling specific API error scenarios.
 */
@Getter
public class ApiExceptionLROA9123 extends RuntimeException {

    private final ErrorCodeLROA9123 errorCode;

    public ApiExceptionLROA9123(ErrorCodeLROA9123 errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ApiExceptionLROA9123(ErrorCodeLROA9123 errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
```
src/main/java/com/yourorg/yourapp/exception/handler/GlobalExceptionHandlerLROA9123.java
```java