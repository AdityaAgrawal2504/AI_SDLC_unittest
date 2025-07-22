package com.example.usersearch.exception;

import com.example.usersearch.enums.ErrorCode_A0B1;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseSearchException_A0B1 extends RuntimeException {
    private final ErrorCode_A0B1 errorCode;

    public DatabaseSearchException_A0B1(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode_A0B1.DATABASE_SEARCH_FAILED;
    }
}
```
src/main/java/com/example/usersearch/model/UserEntity_A0B1.java
```java