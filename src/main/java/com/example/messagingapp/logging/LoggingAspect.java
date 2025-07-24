package com.example.messagingapp.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Log4j2
@RequiredArgsConstructor
public class LoggingAspect {
    
    private final EventLogger eventLogger;

    /**
     * An aspect that logs the start, end, and execution time of methods annotated with @Loggable.
     * It also logs structured events to an abstracted event logger.
     * @param joinPoint The proceeding join point.
     * @return The result of the method execution.
     * @throws Throwable if the executed method throws an exception.
     */
    @Around("@annotation(com.example.messagingapp.logging.Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        log.info("START: {}()", methodName);

        Object result;
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.info("END: {}() - Execution Time: {}ms", methodName, duration);
            
            // Log successful event
            Map<String, Object> details = new HashMap<>();
            details.put("method", methodName);
            details.put("durationMs", duration);
            eventLogger.logInfoEvent("MethodExecutionSuccess", details);

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.error("ERROR: {}() - Execution Time: {}ms - Exception: {}", methodName, duration, e.getMessage());

            // Log error event
            Map<String, Object> details = new HashMap<>();
            details.put("method", methodName);
            details.put("durationMs", duration);
            eventLogger.logErrorEvent("MethodExecutionFailure", details, e);
            
            throw e;
        }
    }
}