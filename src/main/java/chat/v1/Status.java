package chat.v1;

import "google/protobuf/timestamp.proto";

option java_multiple_files = true;
option java_package = "com.example.chat.v1";
option java_outer_classname = "ChatProto";

// The main service for real-time communication
service ChatService {
  // Establishes a long-lived connection for real-time events.
  rpc ConnectStream(stream ClientStreamMessage) returns (stream ServerStreamMessage);
}

// Wrapper message for all possible client-to-server actions.
message ClientStreamMessage {
  oneof event {
    SendMessagePayload send_message = 1;
    ReadReceiptPayload read_receipt = 2;
    TypingIndicator typing_indicator = 3;
  }
}

// Wrapper message for all possible server-to-client events.
message ServerStreamMessage {
  oneof event {
    NewMessagePayload new_message = 1;
    MessageStatusUpdate status_update = 2;
    StreamError error = 3;
    ConnectionAck connection_ack = 4;
  }
}

// Client sends a new message.
message SendMessagePayload {
  string idempotency_key = 1; // UUID to prevent duplicates
  string conversation_id = 2; // Can be a user_id for 1-on-1 chats
  string content = 3;
}

// Client marks messages in a conversation as read.
message ReadReceiptPayload {
  string conversation_id = 1;
  google.protobuf.Timestamp read_up_to_timestamp = 2;
}

// Client indicates they are typing in a conversation.
message TypingIndicator {
  string conversation_id = 1;
  bool is_typing = 2;
}

// Server delivers a new message to the client.
message NewMessagePayload {
  string message_id = 1;
  string conversation_id = 2;
  string sender_id = 3;
  string content = 4;
  google.protobuf.Timestamp timestamp = 5;
}

// Server updates the status of a message (e.g., delivered, seen).
message MessageStatusUpdate {
  string message_id = 1;
  enum Status {
    STATUS_UNSPECIFIED = 0;
    SENT = 1;
    DELIVERED = 2;
    SEEN = 3;
  }
  Status status = 2;
  string user_id_who_updated = 3; // ID of the user who saw the message
}

// Server reports a non-fatal error.
message StreamError {
  int32 code = 1;
  string message = 2;
}

// Server acknowledges a successful connection.
message ConnectionAck {
  string session_id = 1;
  google.protobuf.Timestamp connected_at = 2;
}
```
```java
//
// Filename: src/main/java/com/example/Application.java
//