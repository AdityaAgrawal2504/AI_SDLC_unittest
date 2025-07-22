package com.example.conversation.api.logging;

import com.example.conversation.api.logging.StructuredLoggerFCA1;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Aspect for automatically logging method entry, exit, and execution time.
 * This uses the StructuredLoggerFCA1 to provide consistent, structured logs
 * for performance monitoring and debugging across the application.
 *
 * mermaid
 * sequenceDiagram
 *     autonumber
 *     participant C as Client
 *     participant A as LoggingAspectFCA1
 *     participant S as Service/Controller Method
 *
 *     C->>S: Invoke Method
 *     A->>S: Intercepts call
 *     A->>A: Logs method entry with arguments
 *     A->>A: Starts timer
 *     S->>S: Executes business logic
 *     A->>S: Catches return or exception
 *     A->>A: Stops timer
 *     A->>A: Logs method exit with duration and result
 *     A->>C: Returns result/throws exception
 */
@Aspect
@Component
public class LoggingAspectFCA1 {

    private final StructuredLoggerFCA1 logger;

    public LoggingAspectFCA1(StructuredLoggerFCA1 logger) {
        this.logger = logger;
    }

    /**
     * Defines a pointcut for all public methods within the controller, service, and repository packages.
     */
    @Pointcut("within(com.example.conversation.api.controller..*) || " +
              "within(com.example.conversation.api.service..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Advises methods matching the pointcut, logging their execution.
     *
     * @param proceedingJoinPoint The join point representing the advised method.
     * @return The result of the method execution.
     * @throws Throwable If the advised method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        Map<String, Object> entryContext = new HashMap<>();
        entryContext.put("class", className);
        entryContext.put("method", methodName);
        entryContext.put("event", "method_entry");
        // Optionally add method arguments to context
        // Object[] args = proceedingJoinPoint.getArgs();
        // entryContext.put("arguments", args);

        logger.info(String.format("Entering method: %s.%s", className, methodName), entryContext);

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = proceedingJoinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            long duration = System.currentTimeMillis() - startTime;
            Map<String, Object> errorContext = new HashMap<>();
            errorContext.put("class", className);
            errorContext.put("method", methodName);
            errorContext.put("event", "method_error");
            errorContext.put("executionTimeMs", duration);
            // Optionally add method arguments to error context
            // Object[] args = proceedingJoinPoint.getArgs();
            // errorContext.put("arguments", args);

            logger.error(String.format("Exception in method: %s.%s", className, methodName), errorContext, throwable);
            throw throwable;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            Map<String, Object> exitContext = new HashMap<>();
            exitContext.put("class", className);
            exitContext.put("method", methodName);
            exitContext.put("event", "method_exit");
            exitContext.put("executionTimeMs", duration);

            logger.info(String.format("Exiting method: %s.%s", className, methodName), exitContext);
        }
    }
}
src/main/java/com/example/conversation/api/enums/SortByFCA1.java