package com.example.usersearch.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect to intercept methods annotated with @Loggable_A0B1.
 * It automatically logs the start and end of the method execution, including the total time taken.
 */
@Aspect
@Component
public class LoggingAspect_A0B1 {

    private final LoggerService_A0B1 loggerService;

    public LoggingAspect_A0B1(LoggerService_A0B1 loggerService) {
        this.loggerService = loggerService;
    }

    @Around("@annotation(com.example.usersearch.logging.Loggable_A0B1)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        loggerService.logMethodStart(className, methodName);

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            loggerService.logMethodEnd(className, methodName, endTime - startTime);
            return result;
        } catch (Throwable t) {
            long endTime = System.currentTimeMillis();
            loggerService.logMethodEnd(className, methodName, endTime - startTime);
            loggerService.error(
                String.format("Exception in %s.%s()", className, methodName),
                t,
                null
            );
            throw t;
        }
    }
}
```
src/main/java/com/example/usersearch/security/AuthenticatedUser_A0B1.java
```java