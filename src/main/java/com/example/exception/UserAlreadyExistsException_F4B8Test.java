package com.example.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistsException_F4B8Test {

    @Test
    void testExceptionMessage() {
        String testMessage = "User with phone number 1234567890 already exists.";
        UserAlreadyExistsException_F4B8 exception = new UserAlreadyExistsException_F4B8(testMessage);
        assertEquals(testMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testResponseStatusAnnotation() {
        ResponseStatus annotation = UserAlreadyExistsException_F4B8.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation, "ResponseStatus annotation should be present");
        assertEquals(HttpStatus.CONFLICT, annotation.value(), "HttpStatus should be CONFLICT (409)");
    }

    @Test
    void testIsRuntimeException() {
        assertTrue(new UserAlreadyExistsException_F4B8("test") instanceof RuntimeException);
    }
}
```
```java
src/test/java/com/example/exception/PasswordHashingException_F4B8Test.java