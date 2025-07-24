src/main/java/com/example/provider/interfaces/IPasswordHasher_A1B2C.java
package com.example.provider.interfaces;

public interface IPasswordHasher_A1B2C {
    String hash(String password);
    boolean matches(String rawPassword, String encodedPassword);
}