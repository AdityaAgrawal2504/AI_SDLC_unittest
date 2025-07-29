src/main/java/com/example/logging/LoggingAspect.java

<ctrl60>
<ctrl62>
<ctrl61>
package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect for logging method execution time.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    /**
     * Logs the execution time of methods in the service and controller packages.
     * @param joinPoint The proceeding join point.
     * @return The result of the method execution.
     * @throws Throwable if an error occurs during method execution.
     */
    @Around("execution(* com.example.service..*(..)) || execution(* com.example.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.debug("Executed {}.{} in {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                duration);
        return result;
    }
}