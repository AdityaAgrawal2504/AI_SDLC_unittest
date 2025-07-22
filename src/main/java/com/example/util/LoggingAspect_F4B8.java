package com.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect for structured logging of method execution in controllers and services.
 * Logs method entry, exit, execution time, and arguments.
 */
@Aspect
@Component
public class LoggingAspect_F4B8 {

    // Using a non-static logger to allow for mocking in tests if needed for advanced scenarios
    private final Logger log;
    private final ObjectMapper mapper;

    // Constructor for dependency injection / testability
    public LoggingAspect_F4B8() {
        this(LoggerFactory.getLogger(LoggingAspect_F4B8.class), new ObjectMapper());
    }

    public LoggingAspect_F4B8(Logger log, ObjectMapper mapper) {
        this.log = log;
        this.mapper = mapper;
    }

    /**
     * Pointcut that matches all methods in the controller package.
     */
    @Pointcut("within(com.example.controller..*)")
    public void controllerPointcut() {}

    /**
     * Pointcut that matches all methods in the service implementation package.
     */
    @Pointcut("within(com.example.service.impl..*)")
    public void servicePointcut() {}

    /**
     * Wraps method execution to log entry, exit, and performance details.
     * @param joinPoint The proceeding join point.
     * @return The result of the wrapped method execution.
     * @throws Throwable if the wrapped method throws an exception.
     */
    @Around("controllerPointcut() || servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        try {
            ObjectNode startNode = mapper.createObjectNode();
            startNode.put("event", "method_start");
            startNode.put("class", className);
            startNode.put("method", methodName);
            
            // To prevent sensitive data logging, filter arguments or use a custom serializer.
            // For now, simply mapping all args.
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                // Filter out sensitive arguments like passwords
                Object[] args = joinPoint.getArgs();
                ObjectNode filteredArgs = mapper.createObjectNode();
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    String argName = "arg" + i; // Default name
                    // Attempt to get parameter names if available (requires -parameters compiler flag)
                    try {
                        argName = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getParameterNames()[i];
                    } catch (ClassCastException ignored) {} // Not a method signature, fallback to argI

                    if (argName.toLowerCase().contains("password") && arg instanceof String) {
                        filteredArgs.put(argName, "[REDACTED]");
                    } else {
                        filteredArgs.set(argName, mapper.valueToTree(arg));
                    }
                }
                startNode.set("args", filteredArgs);
            } else {
                startNode.set("args", mapper.createArrayNode());
            }

            log.info(mapper.writeValueAsString(startNode));

            Object result = joinPoint.proceed();
            
            long timeTaken = System.currentTimeMillis() - startTime;
            ObjectNode endNode = mapper.createObjectNode();
            endNode.put("event", "method_end");
            endNode.put("class", className);
            endNode.put("method", methodName);
            endNode.put("executionTimeMs", timeTaken);
            log.info(mapper.writeValueAsString(endNode));

            return result;
        } catch (Throwable t) {
            long timeTaken = System.currentTimeMillis() - startTime;
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("event", "method_error");
            errorNode.put("class", className);
            errorNode.put("method", methodName);
            errorNode.put("executionTimeMs", timeTaken);
            errorNode.put("exception", t.getClass().getName());
            errorNode.put("errorMessage", t.getMessage());
            // Optionally add stack trace
            // errorNode.put("stackTrace", ExceptionUtils.getStackTrace(t)); // Requires Apache Commons Lang 3
            log.error(mapper.writeValueAsString(errorNode));
            throw t;
        }
    }
}
```
```java
src/main/java/com/example/service/IUserRegistrationService_F4B8.java