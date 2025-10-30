package com.auth_service.service;

import com.auth_service.dto.AuthResponse;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    boolean validateToken(String token);
    String extractUsername(String token);
    String getCurrentUserEmail();
    AuthResponse refreshToken(String refreshToken);
}