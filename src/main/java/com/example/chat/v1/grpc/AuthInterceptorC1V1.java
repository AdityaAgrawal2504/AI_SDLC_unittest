package com.example.chat.v1.grpc;

import com.example.chat.v1.service.AuthenticationServiceC1V1;
import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.chat.v1.util.ConstantsC1V1.*;

/**
 * gRPC interceptor to handle Bearer token authentication.
 */
@GrpcGlobalServerInterceptor
public class AuthInterceptorC1V1 implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptorC1V1.class);

    private final AuthenticationServiceC1V1 authenticationService;

    public AuthInterceptorC1V1(AuthenticationServiceC1V1 authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Intercepts incoming calls to validate the auth token and set the user ID in the context.
     */
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(AUTHORIZATION_METADATA_KEY);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.warn("Authentication failed: Missing or invalid Authorization header.");
            call.close(Status.UNAUTHENTICATED.withDescription("Authorization token is missing or invalid"), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        return authenticationService.getUserIdFromToken(token)
            .map(userId -> {
                logger.debug("User authenticated: {}", userId);
                Context context = Context.current().withValue(AUTH_USER_ID_CONTEXT_KEY, userId);
                return Contexts.interceptCall(context, call, headers, next);
            })
            .orElseGet(() -> {
                logger.warn("Authentication failed: Invalid token provided. Token prefix: {}", token.length() > 10 ? token.substring(0, 10) + "..." : token);
                call.close(Status.UNAUTHENTICATED.withDescription("Invalid authentication token"), new Metadata());
                return new ServerCall.Listener<>() {};
            });
    }
}
```
```java
// src/main/java/com/example/chat/v1/grpc/ClientToServerStreamObserverC1V1.java