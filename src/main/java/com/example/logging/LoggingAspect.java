package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("within(com.example.controller..*) || within(com.example.service..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition
    }

    /**
     * Logs method entry, exit, and execution time.
     * @param joinPoint The join point for the advised method.
     * @return The result of the method execution.
     * @throws Throwable if the method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.info("Enter: {}.{}()", className, methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("Exit: {}.{}() with result = {}; Execution time = {} ms", className, methodName, result, (endTime - startTime));
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", java.util.Arrays.toString(joinPoint.getArgs()), className, methodName);
            throw e;
        }
    }
}