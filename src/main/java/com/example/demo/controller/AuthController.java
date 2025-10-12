package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.resource.repository.UserRepository;
import com.example.demo.security.AuthMapper;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody @Valid RegisterRequest r) {
        return ResponseEntity.ok(service.register(r));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest r) {
        return ResponseEntity.ok(service.login(r));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth, UserRepository users) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }
        var u = users.findByEmail(auth.getName()).orElse(null);
        if (u == null) return ResponseEntity.status(404).body(Map.of("error","user_not_found"));
        return ResponseEntity.ok(AuthMapper.toDto(u));
    }
}