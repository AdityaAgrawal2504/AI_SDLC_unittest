package com.fetchmessagesapi.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP Aspect for structured logging of method calls in services and controllers.
 */
@Aspect
@Component
public class LoggingAspectFMA1 {

    private static final Logger log = LogManager.getLogger(LoggingAspectFMA1.class);

    @Pointcut("within(@org.springframework.stereotype.Service *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Logs method entry, exit, execution time, and any exceptions.
     * @param joinPoint The join point for the advised method.
     * @return The result of the method execution.
     * @throws Throwable if the method throws an exception.
     */
    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // Using a structured format
        log.info("Enter: {}.{}() with argument[s] = {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("Exit: {}.{}() with result = {}. Execution time = {} ms", className, methodName, result, (endTime - startTime));
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()), className, methodName);
            throw e;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("Exception in {}.{}(): {}. Execution time = {} ms", className, methodName, e.getMessage(), (endTime - startTime), e);
            throw e;
        }
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/controller/MessageControllerFMA1.java