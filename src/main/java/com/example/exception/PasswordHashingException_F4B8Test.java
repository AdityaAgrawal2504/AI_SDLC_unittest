package com.example.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashingException_F4B8Test {

    @Test
    void testExceptionMessageAndCause() {
        String testMessage = "Failed to hash password due to algorithm error.";
        Throwable testCause = new RuntimeException("Underlying encryption failure");
        
        PasswordHashingException_F4B8 exception = new PasswordHashingException_F4B8(testMessage, testCause);
        
        assertEquals(testMessage, exception.getMessage());
        assertEquals(testCause, exception.getCause());
    }

    @Test
    void testResponseStatusAnnotation() {
        ResponseStatus annotation = PasswordHashingException_F4B8.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation, "ResponseStatus annotation should be present");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, annotation.value(), "HttpStatus should be INTERNAL_SERVER_ERROR (500)");
    }

    @Test
    void testIsRuntimeException() {
        assertTrue(new PasswordHashingException_F4B8("test", new Exception()) instanceof RuntimeException);
    }
}
```
```java
src/test/java/com/example/exception/GlobalExceptionHandler_F4B8Test.java