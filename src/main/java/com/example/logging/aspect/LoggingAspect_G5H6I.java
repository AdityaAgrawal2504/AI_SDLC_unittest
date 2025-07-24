src/main/java/com/example/logging/aspect/LoggingAspect_G5H6I.java
package com.example.logging.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect_G5H6I {

    private static final Logger logger = LogManager.getLogger(LoggingAspect_G5H6I.class);

    /**
     * Pointcut for all methods in the service and controller packages.
     */
    @Pointcut("within(com.example.service..*) || within(com.example.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Logs the execution time of methods annotated with the pointcut.
     * @param joinPoint The proceeding join point.
     * @return The result of the method execution.
     * @throws Throwable if the method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("Enter: {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("Exit: {}.{}() - Execution time: {}ms", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), (endTime - startTime));
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}.{}()", java.util.Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}