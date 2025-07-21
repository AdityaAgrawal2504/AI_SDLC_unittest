package com.yourorg.userregistration.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method entry, exit, and execution time.
 */
@Aspect
@Component
public class LoggingAspectURAPI_1201 {

    private static final Logger logger = LogManager.getLogger(LoggingAspectURAPI_1201.class);

    /**
     * Pointcut that matches all public methods in the service and controller packages.
     */
    @Pointcut("within(com.yourorg.userregistration.service..*) || within(com.yourorg.userregistration.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Advice that logs the execution of methods matched by the pointcut.
     * @param joinPoint The join point for the advised method.
     * @return The result of the method execution.
     * @throws Throwable if the advised method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("Enter: {}.{}()", className, methodName);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exception in {}.{}() with cause = '{}' and exception = '{}'", className, methodName,
                e.getCause() != null ? e.getCause() : "NULL", e.getMessage());
            throw e;
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info("Exit: {}.{}() executed in {} ms", className, methodName, executionTime);
        return result;
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/controller/AuthControllerURAPI_1201.java