package com.example.conversation.history.fch01.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods or classes for automatic structured logging via AOP.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableFCHAPI04 {
}
src/main/java/com/example/conversation/history/fch01/exception/GlobalExceptionHandlerFCHAPI01.java