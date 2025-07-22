package com.example.exception;

import com.example.dto.ApiError_F4B8;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandler_F4B8Test {

    @InjectMocks
    private GlobalExceptionHandler_F4B8 globalExceptionHandler;

    // Mocking the static Logger using reflection or a custom LoggerFactory is complex.
    // For unit tests, often we rely on the method logic and state rather than log output.
    // If strict logging verification is needed, a custom Log4j2 Appender can be used or Powermockito.
    // However, for Spring, it's often more practical to mock the Logger field if it were instance-based,
    // or simply trust LoggerFactory.getLogger in integration tests.
    // For this case, I will assume the log statements are correctly structured and focus on the ResponseEntity.
    // As the Logger is static final, direct mocking with Mockito is not possible without PowerMockito.
    // A workaround for testing logging itself would be a custom appender or system out capture.
    // For the purpose of this task (unit testing functionality), I will make the logger non-static to mock it.
    // (Self-correction: The original code has a static logger. I will make `GlobalExceptionHandler_F4B8`'s logger non-static
    // or skip logger verification if it remains static, focusing on response.
    // Re-evaluating: The prompt did not ask for logger testing, just that tests are complete.
    // Given the difficulty of mocking static loggers, I will NOT modify the class to make it non-static.
    // I will mock `LoggerFactory` and `Logger` if it was instantiated via a non-static factory method, but since it's `static final`,
    // the simplest approach for testing is to ignore the logger output verification for this class
    // and focus on the returned ResponseEntity, or use a capture appender, which is outside basic Mockito.
    // For this example, I'll rely on the ResponseEntity and the assumption that the static logger calls work.)
    @Mock
    private Logger log; // This mock won't work for the static logger directly.

    // A real mock for the static logger would involve more advanced mocking frameworks like PowerMockito
    // or by capturing System.err/System.out, or by configuring a test appender for Log4j2.
    // For standard Mockito, we cannot mock static final fields directly.
    // Let's create a small setup to try to inject the mock log manually if possible, or proceed without verifying log calls.

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        // Attempt to inject mock logger, works if not final static.
        // For static final, this would typically fail or require deeper reflection/PowerMockito.
        // Given the constraints, I will proceed assuming basic functionality tests.
        // No explicit log verification will be done for this specific class.
    }


    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() {
        // Given
        FieldError fieldError1 = new FieldError("objectName", "phoneNumber", "Phone number must be provided.");
        FieldError fieldError2 = new FieldError("objectName", "password", "Password must be at least 8 characters long.");
        
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<ApiError_F4B8> responseEntity = globalExceptionHandler.handleValidationExceptions(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ErrorCode_F4B8.VALIDATION_ERROR.name(), responseEntity.getBody().getErrorCode());
        assertEquals("Input validation failed.", responseEntity.getBody().getMessage());
        assertEquals(2, responseEntity.getBody().getDetails().size());
        assertTrue(responseEntity.getBody().getDetails().containsKey("phoneNumber"));
        assertTrue(responseEntity.getBody().getDetails().containsKey("password"));
        assertEquals("Phone number must be provided.", responseEntity.getBody().getDetails().get("phoneNumber"));
        assertEquals("Password must be at least 8 characters long.", responseEntity.getBody().getDetails().get("password"));
    }

    @Test
    void handleUserAlreadyExistsException_ShouldReturnConflict() {
        // Given
        String errorMessage = "A user with the phone number 1234567890 already exists.";
        UserAlreadyExistsException_F4B8 ex = new UserAlreadyExistsException_F4B8(errorMessage);

        // When
        ResponseEntity<ApiError_F4B8> responseEntity = globalExceptionHandler.handleUserAlreadyExistsException(ex);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ErrorCode_F4B8.USER_ALREADY_EXISTS.name(), responseEntity.getBody().getErrorCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getDetails());
    }

    @Test
    void handlePasswordHashingException_ShouldReturnInternalServerError() {
        // Given
        PasswordHashingException_F4B8 ex = new PasswordHashingException_F4B8("Test hashing error.", new Exception("Cause"));

        // When
        ResponseEntity<ApiError_F4B8> responseEntity = globalExceptionHandler.handlePasswordHashingException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ErrorCode_F4B8.PASSWORD_HASHING_FAILURE.name(), responseEntity.getBody().getErrorCode());
        assertEquals("Could not process registration due to a security configuration error.", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getDetails());
    }

    @Test
    void handleDatabaseException_ShouldReturnInternalServerError() {
        // Given
        DataAccessException ex = new DataIntegrityViolationException("Database constraint violation.");

        // When
        ResponseEntity<ApiError_F4B8> responseEntity = globalExceptionHandler.handleDatabaseException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ErrorCode_F4B8.DATABASE_ERROR.name(), responseEntity.getBody().getErrorCode());
        assertEquals("A database error occurred.", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getDetails());
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerError() {
        // Given
        Exception ex = new RuntimeException("Something unexpected happened.");

        // When
        ResponseEntity<ApiError_F4B8> responseEntity = globalExceptionHandler.handleGlobalException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ErrorCode_F4B8.UNEXPECTED_ERROR.name(), responseEntity.getBody().getErrorCode());
        assertEquals("An unexpected internal server error occurred.", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getDetails());
    }
}
```
```java
src/test/java/com/example/config/WebSecurityConfig_F4B8Test.java