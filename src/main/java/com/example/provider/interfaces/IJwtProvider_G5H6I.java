src/main/java/com/example/provider/interfaces/IJwtProvider_G5H6I.java

<ctrl60>
package com.example.provider.interfaces;

import com.example.dto.SessionDto_M9N0O;
import com.example.model.User_M1N2O;
import io.jsonwebtoken.Claims;
import java.util.UUID;

public interface IJwtProvider_G5H6I {
    SessionDto_M9N0O generateTokens(User_M1N2O user);
    Claims validateToken(String token);
    UUID getUserIdFromToken(String token);
}