package com.example.service;

import com.example.model.User;
import com.example.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final JwtUtils jwtUtils;

    /**
     * Generates an authentication token for a user.
     * @param user The user for whom to generate the token.
     * @return A JWT string.
     */
    @Override
    public String generateToken(User user) {
        return jwtUtils.generateToken(user);
    }
}