src/main/java/com/example/provider/interfaces/ISmsProvider_M9N0O.java

<ctrl60>
package com.example.provider.interfaces;

import java.util.concurrent.CompletableFuture;

public interface ISmsProvider_M9N0O {
    CompletableFuture<Void> send(String to, String message);
}