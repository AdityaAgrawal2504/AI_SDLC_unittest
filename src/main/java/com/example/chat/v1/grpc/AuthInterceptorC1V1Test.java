package com.example.chat.v1.grpc;

import com.example.chat.v1.service.AuthenticationServiceC1V1;
import io.grpc.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.chat.v1.util.ConstantsC1V1.AUTH_USER_ID_CONTEXT_KEY;
import static com.example.chat.v1.util.ConstantsC1V1.AUTHORIZATION_METADATA_KEY;
import static com.example.chat.v1.util.ConstantsC1V1.BEARER_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorC1V1Test {

    @Mock
    private AuthenticationServiceC1V1 authenticationService;
    @Mock
    private ServerCall mockServerCall;
    @Mock
    private Metadata mockHeaders;
    @Mock
    private ServerCallHandler mockServerCallHandler;
    @Mock
    private ServerCall.Listener mockServerCallListener;
    @Mock
    private Context.CancellableContext mockContext;

    private AuthInterceptorC1V1 authInterceptor;

    @BeforeEach
    void setUp() {
        authInterceptor = new AuthInterceptorC1V1(authenticationService);

        // Mock common behavior for ServerCallHandler
        when(mockServerCallHandler.startCall(any(), any())).thenReturn(mockServerCallListener);

        // Mock Context.current() and its chaining for when AUTH_USER_ID_CONTEXT_KEY is set
        try (var ignored = io.grpc.Context.current().withValue(AUTH_USER_ID_CONTEXT_KEY, null).attach()) {
            // This setup allows mocking Context.current().withValue calls
            // For now, simply ensure Contexts.interceptCall is called with correct context
            // if authentication succeeds.
        }
    }

    @Test
    void interceptCall_ValidToken_ContextSetAndCallProceeds() {
        // Arrange
        String userId = "user_test123";
        String token = userId; // Mock implementation assumes token is user ID
        String authHeader = BEARER_PREFIX + token;

        when(mockHeaders.get(AUTHORIZATION_METADATA_KEY)).thenReturn(authHeader);
        when(authenticationService.getUserIdFromToken(token)).thenReturn(Optional.of(userId));

        // Mock Contexts.interceptCall to return a listener
        try (MockedStatic<Context> mockedContext = mockStatic(Context.class)) {
            mockedContext.when(Context::current).thenReturn(Context.ROOT);
            mockedContext.when(() -> Context.ROOT.withValue(AUTH_USER_ID_CONTEXT_KEY, userId)).thenReturn(mockContext);
            mockedContext.when(() -> Contexts.interceptCall(mockContext, mockServerCall, mockHeaders, mockServerCallHandler)).thenReturn(mockServerCallListener);

            // Act
            ServerCall.Listener<Object> listener = authInterceptor.interceptCall(mockServerCall, mockHeaders, mockServerCallHandler);

            // Assert
            assertNotNull(listener);
            assertEquals(mockServerCallListener, listener);
            verify(mockHeaders, times(1)).get(AUTHORIZATION_METADATA_KEY);
            verify(authenticationService, times(1)).getUserIdFromToken(token);
            verify(mockServerCall, never()).close(any(), any());
            mockedContext.verify(() -> Context.ROOT.withValue(AUTH_USER_ID_CONTEXT_KEY, userId), times(1));
            mockedContext.verify(() -> Contexts.interceptCall(mockContext, mockServerCall, mockHeaders, mockServerCallHandler), times(1));
        }
    }

    @Test
    void interceptCall_MissingAuthorizationHeader_ReturnsUnauthenticatedStatus() {
        // Arrange
        when(mockHeaders.get(AUTHORIZATION_METADATA_KEY)).thenReturn(null);

        // Act
        ServerCall.Listener<Object> listener = authInterceptor.interceptCall(mockServerCall, mockHeaders, mockServerCallHandler);

        // Assert
        assertNotNull(listener);
        verify(mockHeaders, times(1)).get(AUTHORIZATION_METADATA_KEY);
        verify(authenticationService, never()).getUserIdFromToken(anyString());
        ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(Status.class);
        verify(mockServerCall, times(1)).close(statusCaptor.capture(), any(Metadata.class));
        assertEquals(Status.UNAUTHENTICATED.getCode(), statusCaptor.getValue().getCode());
        assertEquals("Authorization token is missing or invalid", statusCaptor.getValue().getDescription());
    }

    @Test
    void interceptCall_InvalidAuthorizationHeaderFormat_ReturnsUnauthenticatedStatus() {
        // Arrange
        String authHeader = "InvalidTokenFormat abc"; // Does not start with "Bearer "
        when(mockHeaders.get(AUTHORIZATION_METADATA_KEY)).thenReturn(authHeader);

        // Act
        ServerCall.Listener<Object> listener = authInterceptor.interceptCall(mockServerCall, mockHeaders, mockServerCallHandler);

        // Assert
        assertNotNull(listener);
        verify(mockHeaders, times(1)).get(AUTHORIZATION_METADATA_KEY);
        verify(authenticationService, never()).getUserIdFromToken(anyString());
        ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(Status.class);
        verify(mockServerCall, times(1)).close(statusCaptor.capture(), any(Metadata.class));
        assertEquals(Status.UNAUTHENTICATED.getCode(), statusCaptor.getValue().getCode());
        assertEquals("Authorization token is missing or invalid", statusCaptor.getValue().getDescription());
    }

    @Test
    void interceptCall_InvalidToken_ReturnsUnauthenticatedStatus() {
        // Arrange
        String invalidToken = "nonExistentUser";
        String authHeader = BEARER_PREFIX + invalidToken;

        when(mockHeaders.get(AUTHORIZATION_METADATA_KEY)).thenReturn(authHeader);
        when(authenticationService.getUserIdFromToken(invalidToken)).thenReturn(Optional.empty());

        // Act
        ServerCall.Listener<Object> listener = authInterceptor.interceptCall(mockServerCall, mockHeaders, mockServerCallHandler);

        // Assert
        assertNotNull(listener);
        verify(mockHeaders, times(1)).get(AUTHORIZATION_METADATA_KEY);
        verify(authenticationService, times(1)).getUserIdFromToken(invalidToken);
        ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(Status.class);
        verify(mockServerCall, times(1)).close(statusCaptor.capture(), any(Metadata.class));
        assertEquals(Status.UNAUTHENTICATED.getCode(), statusCaptor.getValue().getCode());
        assertEquals("Invalid authentication token", statusCaptor.getValue().getDescription());
    }
}
```
```java
// src/test/java/com/example/chat/v1/grpc/ClientToServerStreamObserverC1V1Test.java