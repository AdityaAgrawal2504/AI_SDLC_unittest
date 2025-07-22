package com.example.conversation.history.fch01.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Aspect to log method entry, exit, and execution time for annotated methods.
 */
@Aspect
@Component
public class LoggingAspectFCHAPI03 {

    private final StructuredLoggerFCHAPI01 structuredLogger;

    public LoggingAspectFCHAPI03(StructuredLoggerFCHAPI01 structuredLogger) {
        this.structuredLogger = structuredLogger;
    }

    @Around("within(@com.example.conversation.history.fch01.logging.LoggableFCHAPI04 *) || @annotation(com.example.conversation.history.fch01.logging.LoggableFCHAPI04)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        Map<String, Object> startContext = new HashMap<>();
        startContext.put("class", className);
        startContext.put("method", methodName);
        structuredLogger.logDebug("Entering method.", startContext);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            Map<String, Object> errorContext = new HashMap<>();
            errorContext.put("class", className);
            errorContext.put("method", methodName);
            errorContext.put("durationMs", duration);
            structuredLogger.logError("Exception in method.", e, errorContext);
            throw e;
        }

        long duration = System.currentTimeMillis() - startTime;
        Map<String, Object> endContext = new HashMap<>();
        endContext.put("class", className);
        endContext.put("method", methodName);
        endContext.put("durationMs", duration);
        structuredLogger.logDebug("Exiting method.", endContext);

        return result;
    }
}
src/main/java/com/example/conversation/history/fch01/logging/LoggableFCHAPI04.java