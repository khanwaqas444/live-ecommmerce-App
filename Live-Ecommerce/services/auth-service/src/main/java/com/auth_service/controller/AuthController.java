package com.auth_service.controller;

import com.auth_service.dto.AuthResponse;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.RegisterRequest;
import com.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ✅ REGISTER — create new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()));
        }
    }

    // ✅ LOGIN — authenticate and return JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()));
        }
    }

    // ✅ VALIDATE TOKEN — check if JWT is valid (for other microservices)
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken() {
        // If this endpoint is reached, the JWT filter already validated the token
        // Spring Security context will contain the authenticated user
        return ResponseEntity.ok(Map.of("valid", true));
    }

    // ✅ ME — get user info from authenticated context
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            // Get the authenticated user from Spring Security context
            String email = authService.getCurrentUserEmail();
            return ResponseEntity.ok(Map.of("email", email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        try {
            AuthResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }


    // ✅ EXAMPLE PROTECTED ENDPOINT — demonstrates how other endpoints should work
    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        try {
            String email = authService.getCurrentUserEmail();
            return ResponseEntity.ok(Map.of(
                    "message", "This is a protected endpoint",
                    "authenticatedUser", email,
                    "timestamp", System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()));
        }
    }
}
