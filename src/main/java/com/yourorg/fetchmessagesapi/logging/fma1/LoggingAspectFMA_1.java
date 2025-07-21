package com.yourorg.fetchmessagesapi.logging.fma1;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * AOP Aspect to intercept methods annotated with @LoggableFMA_1 for structured logging.
 */
@Aspect
@Component
@Log4j2
public class LoggingAspectFMA_1 {

    @Around("@annotation(com.yourorg.fetchmessagesapi.logging.fma1.LoggableFMA_1)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("event", "method_start");
        logEntry.put("method", methodName);
        logEntry.put("params", getMethodParams(joinPoint));

        log.info(logEntry);

        Object result;
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logEntry.put("event", "method_end");
            logEntry.put("duration_ms", endTime - startTime);
            log.info(logEntry);
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logEntry.put("event", "method_exception");
            logEntry.put("duration_ms", endTime - startTime);
            logEntry.put("exception_type", throwable.getClass().getName());
            logEntry.put("exception_message", throwable.getMessage());
            log.error(logEntry, throwable);
            throw throwable;
        }
    }

    private Map<String, Object> getMethodParams(ProceedingJoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        for (int i = 0; i < paramNames.length; i++) {
            // Avoid logging sensitive information like tokens
            if (paramNames[i].toLowerCase().contains("token") || paramNames[i].toLowerCase().contains("authorization")) {
                params.put(paramNames[i], "******");
            } else {
                params.put(paramNames[i], paramValues[i]);
            }
        }
        return params;
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/repository/fma1/ConversationRepositoryFMA_1.java