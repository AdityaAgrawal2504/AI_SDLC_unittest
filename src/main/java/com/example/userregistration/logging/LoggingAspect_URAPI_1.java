package com.example.userregistration.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP Aspect for logging method entry, exit, and execution time.
 */
@Aspect
@Component
public class LoggingAspect_URAPI_1 {

    private static final Logger log = LogManager.getLogger(LoggingAspect_URAPI_1.class);

    /**
     * Defines a pointcut for all public methods in the controller, service, and repository packages.
     */
    @Pointcut("within(com.example.userregistration.controller..*) || " +
              "within(com.example.userregistration.service..*) || " +
              "within(com.example.userregistration.repository..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Logs method execution details.
     * @param joinPoint The join point for the advised method.
     * @return The result of the method execution.
     * @throws Throwable if the advised method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] methodArgs = joinPoint.getArgs();

        if (log.isInfoEnabled()) {
            log.info("START: {}() with args = {}", methodName, Arrays.toString(methodArgs));
        }

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("EXCEPTION in {}: {}", methodName, throwable.getMessage());
            throw throwable;
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        if (log.isInfoEnabled()) {
            log.info("END: {}() completed in {} ms", methodName, timeTaken);
        }
        return result;
    }
}
```
src/main/java/com/example/userregistration/service/IUserRegistrationService_URAPI_1.java
```java