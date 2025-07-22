package com.example.chat.v1.grpc;

import com.example.chat.v1.enums.ErrorEventCodeC1V1;
import com.example.chat.v1.exception.ChatValidationExceptionC1V1;
import com.example.chat.v1.logging.AbstractLoggerC1V1;
import com.example.chat.v1.service.ChatLogicServiceC1V1;
import com.example.chat.v1.service.EventDispatchServiceC1V1;
import com.example.chat.v1.service.UserStreamManagerC1V1;
import com.example.chat.v1.validation.RequestValidatorC1V1;
import io.grpc.stub.StreamObserver;

import java.util.Map;

/**
 * Handles the lifecycle of an individual client's incoming stream.
 */
public class ClientToServerStreamObserverC1V1 implements StreamObserver<ClientToServerEvent> {
    
    private final String userId;
    private final StreamObserver<ServerToClientEvent> responseObserver;
    private final UserStreamManagerC1V1 userStreamManager;
    private final RequestValidatorC1V1 requestValidator;
    private final ChatLogicServiceC1V1 chatLogicService;
    private final EventDispatchServiceC1V1 eventDispatchService;
    private final AbstractLoggerC1V1 logger;

    public ClientToServerStreamObserverC1V1(
            String userId,
            StreamObserver<ServerToClientEvent> responseObserver,
            UserStreamManagerC1V1 userStreamManager,
            RequestValidatorC1V1 requestValidator,
            ChatLogicServiceC1V1 chatLogicService,
            EventDispatchServiceC1V1 eventDispatchService,
            AbstractLoggerC1V1 logger) {
        this.userId = userId;
        this.responseObserver = responseObserver;
        this.userStreamManager = userStreamManager;
        this.requestValidator = requestValidator;
        this.chatLogicService = chatLogicService;
        this.eventDispatchService = eventDispatchService;
        this.logger = logger;
    }

    /**
     * Handles a new event from the client.
     */
    @Override
    public void onNext(ClientToServerEvent event) {
        Map<String, Object> logContext = Map.of("userId", userId, "eventType", event.getEventCase().toString());
        try {
            logger.logInfo("Received client event", logContext);
            requestValidator.validate(event);

            switch (event.getEventCase()) {
                case SEND_MESSAGE_REQUEST:
                    chatLogicService.processSendMessage(event.getSendMessageRequest(), userId);
                    break;
                case MARK_AS_SEEN_REQUEST:
                    chatLogicService.processMarkAsSeen(event.getMarkAsSeenRequest(), userId);
                    break;
                case START_TYPING_REQUEST:
                    chatLogicService.processTyping(event.getStartTypingRequest().getConversationId(), userId, true);
                    break;
                case STOP_TYPING_REQUEST:
                    chatLogicService.processTyping(event.getStopTypingRequest().getConversationId(), userId, false);
                    break;
                default:
                    // This case is already handled by the validator, but included for completeness.
                    throw new ChatValidationExceptionC1V1("Unknown event type", ErrorEventCodeC1V1.VALIDATION_FAILED, null, "UnknownEvent");
            }
        } catch (ChatValidationExceptionC1V1 e) {
            logger.logWarn("Client event validation failed", Map.of("userId", userId, "error", e.getMessage(), "errorCode", e.getErrorCode().name(), "clientMessageId", e.getClientMessageId() != null ? e.getClientMessageId() : "N/A"));
            ServerToClientEvent errorEvent = eventDispatchService.buildErrorEvent(e.getErrorCode().name(), e.getMessage(), e.getClientMessageId(), e.getOriginalRequestType());
            responseObserver.onNext(errorEvent);
        } catch (Exception e) {
            logger.logError("Unexpected error processing client event", logContext, e);
            ServerToClientEvent errorEvent = eventDispatchService.buildErrorEvent(ErrorEventCodeC1V1.UNKNOWN_ERROR.name(), "An internal error occurred", null, event.getEventCase().toString());
            responseObserver.onNext(errorEvent);
        }
    }

    /**
     * Handles a client-side error or stream closure.
     */
    @Override
    public void onError(Throwable t) {
        logger.logWarn("Client stream error", Map.of("userId", userId), t);
        userStreamManager.removeStream(userId);
    }

    /**
     * Handles the completion of the client's stream.
     */
    @Override
    public void onCompleted() {
        logger.logInfo("Client stream completed", Map.of("userId", userId));
        userStreamManager.removeStream(userId);
        responseObserver.onCompleted();
    }
}
```
```java
// src/main/java/com/example/chat/v1/grpc/ChatServiceV1Impl_C1V1.java