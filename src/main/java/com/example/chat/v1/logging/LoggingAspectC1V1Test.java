package com.example.chat.v1.logging;

import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectC1V1Test {

    @Mock
    private AbstractLoggerC1V1 logger;

    @InjectMocks
    private LoggingAspectC1V1 loggingAspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    void setUp() throws Throwable {
        // Reset ThreadContext for each test
        ThreadContext.clearAll();

        // Common mocks for joinPoint setup
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(TestClass.class);
        when(methodSignature.getName()).thenReturn("testMethod");
    }

    @Test
    void logMethodExecution_Success_LogsStartEndAndDuration() throws Throwable {
        // Arrange
        when(proceedingJoinPoint.proceed()).thenReturn("result");

        // Act
        Object result = loggingAspect.logMethodExecution(proceedingJoinPoint);

        // Assert
        assertEquals("result", result);

        // Capture arguments for logInfo calls
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> contextCaptor = ArgumentCaptor.forClass(Map.class);
        verify(logger, times(2)).logInfo(messageCaptor.capture(), contextCaptor.capture());
        verify(logger, never()).logError(anyString(), anyMap(), any(Throwable.class));

        // Verify start log
        assertEquals("Executing method", messageCaptor.getAllValues().get(0));
        Map<String, Object> startContext = contextCaptor.getAllValues().get(0);
        assertEquals("TestClass.testMethod", startContext.get("method"));
        assertEquals("METHOD_START", startContext.get("eventType"));

        // Verify end log
        assertEquals("Finished method execution", messageCaptor.getAllValues().get(1));
        Map<String, Object> endContext = contextCaptor.getAllValues().get(1);
        assertEquals("TestClass.testMethod", endContext.get("method"));
        assertEquals("METHOD_END", endContext.get("eventType"));
        assertTrue(endContext.containsKey("durationMs"));
        assertTrue((Long) endContext.get("durationMs") >= 0);

        // Ensure ThreadContext is empty at the end
        assertTrue(ThreadContext.isEmpty());
    }

    @Test
    void logMethodExecution_Exception_LogsErrorAndRethrows() throws Throwable {
        // Arrange
        RuntimeException testException = new RuntimeException("Simulated error");
        when(proceedingJoinPoint.proceed()).thenThrow(testException);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                loggingAspect.logMethodExecution(proceedingJoinPoint)
        );
        assertEquals(testException, thrown);

        // Verify logs
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> contextCaptor = ArgumentCaptor.forClass(Map.class);
        verify(logger, times(1)).logInfo(messageCaptor.capture(), contextCaptor.capture()); // Only start log
        verify(logger, times(1)).logError(messageCaptor.capture(), contextCaptor.capture(), eq(testException)); // Error log

        // Verify start log
        assertEquals("Executing method", messageCaptor.getAllValues().get(0));
        Map<String, Object> startContext = contextCaptor.getAllValues().get(0);
        assertEquals("TestClass.testMethod", startContext.get("method"));
        assertEquals("METHOD_START", startContext.get("eventType"));

        // Verify error log
        assertEquals("Exception in method", messageCaptor.getAllValues().get(1));
        Map<String, Object> errorContext = contextCaptor.getAllValues().get(1);
        assertEquals("TestClass.testMethod", errorContext.get("method"));
        assertEquals("METHOD_ERROR", errorContext.get("eventType"));

        // Verify end log is also called in finally block
        verify(logger, times(2)).logInfo(anyString(), anyMap()); // start and end log
        
        // The last logInfo call should be the "Finished method execution" from the finally block
        assertEquals("Finished method execution", messageCaptor.getAllValues().get(2));
        Map<String, Object> endContext = contextCaptor.getAllValues().get(2);
        assertEquals("TestClass.testMethod", endContext.get("method"));
        assertEquals("METHOD_END", endContext.get("eventType"));
        assertTrue(endContext.containsKey("durationMs"));


        // Ensure ThreadContext is empty at the end
        assertTrue(ThreadContext.isEmpty());
    }

    // Dummy class to simulate a class with a method to be advised
    static class TestClass {
        @LoggableC1V1
        public void testMethod() {
            // This method body is not actually executed in the unit test,
            // only the proceed() call on the joinPoint is mocked.
        }
    }
}
```
```java
// src/test/java/com/example/chat/v1/grpc/AuthInterceptorC1V1Test.java