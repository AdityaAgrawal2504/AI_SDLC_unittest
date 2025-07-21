package com.auth.api.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * AOP Aspect to intercept methods annotated with @UserRegAPI_Loggable and log their execution.
 */
@Aspect
@Component
public class UserRegAPI_LoggingAspect_33A1 {

    private static final Logger logger = LogManager.getLogger(UserRegAPI_LoggingAspect_33A1.class);

    /**
     * Intercepts and logs method execution details.
     * @param point The join point for the intercepted method.
     * @return The result of the method execution.
     * @throws Throwable if the method throws an exception.
     */
    @Around("@annotation(com.auth.api.logging.UserRegAPI_Loggable_33A1)")
    public Object logExecutionTime(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) point.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("event", "method_execution_start");
        logMap.put("class", className);
        logMap.put("method", methodName);
        
        logger.info(logMap);
        
        Object result = point.proceed();
        
        long executionTime = System.currentTimeMillis() - start;

        logMap.put("event", "method_execution_end");
        logMap.put("executionTimeMs", executionTime);
        logger.info(logMap);
        
        return result;
    }
}
```
```java
// src/main/java/com/auth/api/service/UserRegAPI_AuthService_33A1.java