package com.yourorg.yourapp.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for logging the execution of service and controller methods.
 */
@Aspect
@Component
@Log4j2
public class LoggingAspectLROA9123 {

    /**
     * Pointcut that matches all methods in the service and controller packages.
     */
    @Pointcut("within(com.yourorg.yourapp.service..*) || within(com.yourorg.yourapp.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Advice that logs method entry, exit, and execution time.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}. Execution time = {} ms", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result, (endTime - startTime));
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
```
src/main/java/com/yourorg/yourapp/service/AuthServiceLROA9123.java