package com.example.auth.initiate.api_IA_9F3E.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Aspect for logging method entry, exit, and execution time.
 * This provides a clean, cross-cutting way to implement structured logging.
 */
@Aspect
@Component
public class LoggingAspect_IA_9F3E {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect_IA_9F3E.class);

    /**
     * Logs execution details for methods in the controller, service, and repository packages.
     * @param joinPoint The point at which the aspect is applied.
     * @return The result of the original method call.
     * @throws Throwable if the joined point throws an exception.
     */
    @Around("execution(* com.example.auth.initiate.api_IA_9F3E.controller..*(..)) || " +
            "execution(* com.example.auth.initiate.api_IA_9F3E.service..*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();

        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("class", className);
        logDetails.put("method", methodName);

        logger.info("Enter: {}.{}()", className, methodName);
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            logDetails.put("executionTimeMs", executionTime);
            logger.info("Exit: {}.{}()", className, methodName);
            logger.debug("Method execution details: {}", logDetails);
            
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            logDetails.put("executionTimeMs", executionTime);
            logDetails.put("exceptionType", throwable.getClass().getSimpleName());
            logDetails.put("exceptionMessage", throwable.getMessage());
            
            logger.error("Exception in {}.{}()", className, methodName, throwable);
            logger.error("Method execution failed: {}", logDetails);

            throw throwable;
        }
    }
}
```
```java