package com.mycompany.chatapp.grpc;

import "google/protobuf/timestamp.proto";

option java_multiple_files = true;
option java_package = "com.mycompany.chatapp.grpc";
option java_outer_classname = "RealTimeChatStreamAPIProto_1122";


// Service for real-time, bi-directional chat communication.
service ChatService {
  // Establishes a bi-directional stream for sending new messages and
  // receiving real-time messages and status updates from other users.
  rpc ChatStream(stream ClientStreamRequest) returns (stream ServerStreamResponse);
}

// Represents the real-time status of a user within a chat.
enum UserStatus {
  STATUS_UNSPECIFIED = 0;
  ONLINE = 1;
  AWAY = 2;
  TYPING = 3;
}

// Represents the type of acknowledgement for a message.
enum ReceiptType {
  RECEIPT_UNSPECIFIED = 0;
  DELIVERED = 1;
  READ = 2;
}

// Wrapper message sent from the client to the server.
// The first message on the stream MUST contain 'initial_connect'.
message ClientStreamRequest {
  oneof payload {
    InitialConnectRequest initial_connect = 1;
    SendMessageRequest send_message = 2;
    SendStatusUpdateRequest send_status_update = 3;
    SendMessageReceiptRequest send_message_receipt = 4;
  }
}

// The first message a client must send to authenticate and join a chat stream.
message InitialConnectRequest {
  // Authentication token (e.g., JWT) for the user session. REQUIRED.
  string session_token = 1;
  // The unique identifier of the chat to connect to. REQUIRED, UUID format.
  string chat_id = 2;
}

// Request to send a new text message to the chat.
message SendMessageRequest {
  // A client-generated UUID to ensure message processing is idempotent. REQUIRED.
  string idempotency_key = 1;
  // The unique identifier of the target chat. REQUIRED, UUID format.
  string chat_id = 2;
  // The text content of the message. REQUIRED, 1-4096 chars.
  string content = 3;
  // Optional ID of the message this one is replying to, for threading. UUID format.
  optional string parent_message_id = 4;
}

// Request to broadcast the user's status (e.g., typing) to the chat.
message SendStatusUpdateRequest {
  // The unique identifier of the target chat. REQUIRED, UUID format.
  string chat_id = 1;
  // The new status of the user. REQUIRED.
  UserStatus status = 2;
}

// Request to send a receipt for a message (e.g., delivered, read).
message SendMessageReceiptRequest {
  // The unique identifier of the chat where the message is. REQUIRED, UUID format.
  string chat_id = 1;
  // The ID of the message being acknowledged. REQUIRED, UUID format.
  string message_id = 2;
  // The type of the receipt. REQUIRED.
  ReceiptType receipt_type = 3;
}

// A wrapper message sent from the server to the client.
message ServerStreamResponse {
  oneof payload {
    StreamConnectedEvent stream_connected = 1;
    NewMessageEvent new_message = 2;
    StatusUpdateEvent status_update = 3;
    MessageReceiptEvent message_receipt = 4;
    ErrorEvent error = 5;
  }
}

// Sent by the server as the first message to confirm a successful connection.
message StreamConnectedEvent {
  string message = 1;
  google.protobuf.Timestamp server_time = 2;
}

// Event pushed to clients when a new message is posted in the chat.
message NewMessageEvent {
  string message_id = 1;
  string chat_id = 2;
  string sender_id = 3;
  string sender_display_name = 4;
  string content = 5;
  google.protobuf.Timestamp timestamp = 6;
  optional string parent_message_id = 7;
}

// Event pushed to clients when a user's status changes.
message StatusUpdateEvent {
  string chat_id = 1;
  string user_id = 2;
  UserStatus status = 3;
  google.protobuf.Timestamp timestamp = 4;
}

// Event pushed to clients when a message receipt is processed.
message MessageReceiptEvent {
  string chat_id = 1;
  string message_id = 2;
  string user_id = 3;
  ReceiptType receipt_type = 4;
  google.protobuf.Timestamp timestamp = 5;
}

// A non-fatal, application-level error pushed to the client.
message ErrorEvent {
  int32 code = 1;
  string message = 2;
  optional string idempotency_key = 3;
}
```
```properties
# src/main/resources/application.properties
# gRPC Server Configuration
grpc.server.port=9090

# For testing, we can disable authentication checks
# chat.security.enabled=false

# Logging configuration
logging.config=classpath:log4j2.xml
```
```xml
<!-- src/main/resources/log4j2.xml -->