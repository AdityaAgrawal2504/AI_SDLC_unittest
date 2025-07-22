package com.example.chat.v1.logging;

import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Aspect to intercept methods annotated with @LoggableC1V1 for automated logging.
 */
@Aspect
@Component
public class LoggingAspectC1V1 {

    private final AbstractLoggerC1V1 logger;

    public LoggingAspectC1V1(AbstractLoggerC1V1 logger) {
        this.logger = logger;
    }

    /**
     * Logs the entry, exit, and execution time of the annotated method.
     */
    @Around("@annotation(com.example.chat.v1.logging.LoggableC1V1)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        try {
            Map<String, Object> startContext = new HashMap<>();
            startContext.put("method", methodName);
            startContext.put("eventType", "METHOD_START");
            logger.logInfo("Executing method", startContext);

            Object result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            Map<String, Object> errorContext = new HashMap<>();
            errorContext.put("method", methodName);
            errorContext.put("eventType", "METHOD_ERROR");
            logger.logError("Exception in method", errorContext, e);
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, Object> endContext = new HashMap<>();
            endContext.put("method", methodName);
            endContext.put("eventType", "METHOD_END");
            endContext.put("durationMs", duration);
            logger.logInfo("Finished method execution", endContext);
        }
    }
}
```
```java
// src/main/java/com/example/chat/v1/util/ConstantsC1V1.java