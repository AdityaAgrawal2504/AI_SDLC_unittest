package com.example.grpc;

import com.example.security.JwtUtil_SEC_33DD;
import io.grpc.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * gRPC server interceptor to handle authentication via JWT in metadata.
 */
@GrpcGlobalServerInterceptor
@RequiredArgsConstructor
public class AuthInterceptor_GRPC_4001 implements ServerInterceptor {

    private final JwtUtil_SEC_33DD jwtUtil;
    private final UserDetailsService userDetailsService;

    public static final Metadata.Key<String> AUTHORIZATION_HEADER_KEY = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Intercepts a gRPC call to perform authentication.
     */
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(AUTHORIZATION_HEADER_KEY);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            call.close(Status.UNAUTHENTICATED.withDescription("Authorization token is missing or invalid."), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        try {
            if (jwtUtil.isTokenValid(token)) {
                String userId = jwtUtil.extractUserId(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // Store authentication in both gRPC context and Spring Security context
                Context ctx = Context.current().withValue(GrpcAuthContext_GRPC_4002.AUTHENTICATION_KEY, authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return Contexts.interceptCall(ctx, call, headers, next);
            } else {
                 call.close(Status.UNAUTHENTICATED.withDescription("Invalid JWT token."), new Metadata());
                 return new ServerCall.Listener<>() {};
            }
        } catch (Exception e) {
            call.close(Status.UNAUTHENTICATED.withDescription("Authentication failed: " + e.getMessage()), new Metadata());
            return new ServerCall.Listener<>() {};
        }
    }
}
```
```java
//
// Filename: src/main/java/com/example/grpc/GrpcAuthContext_GRPC_4002.java
//