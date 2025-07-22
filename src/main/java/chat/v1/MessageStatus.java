package chat.v1;

import "google/protobuf/timestamp.proto";
import "google/protobuf/struct.proto";

option java_multiple_files = true;
option java_package = "com.example.chat.v1.grpc";
option java_outer_classname = "ChatServiceProto";

// The main chat service definition
service ChatService {
  // Establishes a bi-directional stream for real-time messaging.
  rpc MessageStream(stream ClientToServerEvent) returns (stream ServerToClientEvent);
}

// Wrapper message for all events from client to server
message ClientToServerEvent {
  oneof event {
    SendMessageRequest send_message_request = 1;
    MarkAsSeenRequest mark_as_seen_request = 2;
    StartTypingRequest start_typing_request = 3;
    StopTypingRequest stop_typing_request = 4;
  }
}

// Wrapper message for all events from server to client
message ServerToClientEvent {
  oneof event {
    StreamEstablishedEvent stream_established_event = 1;
    NewMessageEvent new_message_event = 2;
    MessageDeliveredEvent message_delivered_event = 3;
    MessageSeenEvent message_seen_event = 4;
    UserTypingEvent user_typing_event = 5;
    ErrorEvent error_event = 6;
  }
}

// Client event: send a new message
message SendMessageRequest {
  string client_message_id = 1; // UUIDv4
  string conversation_id = 2;
  string content = 3;
}

// Client event: mark messages as seen
message MarkAsSeenRequest {
  string conversation_id = 1;
  string last_seen_message_id = 2;
}

// Client event: user started typing
message StartTypingRequest {
  string conversation_id = 1;
}

// Client event: user stopped typing
message StopTypingRequest {
  string conversation_id = 1;
}

// Server event: stream successfully established
message StreamEstablishedEvent {
  string user_id = 1;
  google.protobuf.Timestamp server_timestamp = 2;
}

// Server event: a new message has arrived
message NewMessageEvent {
  Message message = 1;
}

// Server event: a message was delivered
message MessageDeliveredEvent {
  string message_id = 1;
  string conversation_id = 2;
  string recipient_id = 3;
  google.protobuf.Timestamp delivered_at = 4;
}

// Server event: a message was seen
message MessageSeenEvent {
  string conversation_id = 1;
  string seen_by_user_id = 2;
  string last_seen_message_id = 3;
  google.protobuf.Timestamp seen_at = 4;
}

// Server event: a user is typing
message UserTypingEvent {
  string conversation_id = 1;
  User user = 2;
}

// Server event: a non-terminal error occurred
message ErrorEvent {
  string error_code = 1;
  string error_message = 2;
  google.protobuf.Struct details = 3;
}

// Data structure: A single chat message
message Message {
  string id = 1;
  string client_message_id = 2;
  string conversation_id = 3;
  User sender = 4;
  string content = 5;
  google.protobuf.Timestamp created_at = 6;
  MessageStatus status = 7;
}

// Data structure: A user
message User {
  string id = 1;
  string display_name = 2;
  string avatar_url = 3;
}

// Enum: Message delivery status
enum MessageStatus {
  MESSAGE_STATUS_UNSPECIFIED = 0;
  SENDING = 1;
  SENT = 2;
  DELIVERED = 3;
  SEEN = 4;
  FAILED = 5;
}
```
```properties
# src/main/resources/application.properties
# gRPC Server Configuration
grpc.server.port=9090
grpc.server.security.enabled=false

# App specific
chat.service.jwt.secret=your-super-secret-and-long-jwt-key-for-hs256-or-higher
chat.service.jwt.issuer=my-chat-app
```
```xml
<!-- src/main/resources/log4j2.xml -->