package com.example.auth.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AOP Aspect for logging method execution time and details.
 */
@Aspect
@Component
public class LoggingAspect_AuthVerifyOtp_17169 {

    private final StructuredLogger_AuthVerifyOtp_17169 logger;

    public LoggingAspect_AuthVerifyOtp_17169(StructuredLogger_AuthVerifyOtp_17169 logger) {
        this.logger = logger;
    }

    /**
     * Defines a pointcut for methods within the controller, service, and repository packages.
     */
    @Pointcut("within(com.example.auth.controller..*) || within(com.example.auth.service..*) || within(com.example.auth.repository..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition
    }

    /**
     * Logs the start, end, and duration of method execution around the defined pointcut.
     * @param joinPoint The proceeding join point.
     * @return The result of the method execution.
     * @throws Throwable if the underlying method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.logInfo("Enter: " + className + "." + methodName + "()", Map.of(
            "class", className,
            "method", methodName,
            "event", "enter"
        ));

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            logger.logInfo("Exit: " + className + "." + methodName + "()", Map.of(
                "class", className,
                "method", methodName,
                "event", "exit",
                "durationMs", timeTaken
            ));
            return result;
        } catch (Throwable t) {
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            logger.logError("Exception in " + className + "." + methodName + "()", Map.of(
                "class", className,
                "method", methodName,
                "event", "exception",
                "durationMs", timeTaken
            ), t);
            throw t;
        }
    }
}
src/main/java/com/example/auth/service/OtpService_17169.java