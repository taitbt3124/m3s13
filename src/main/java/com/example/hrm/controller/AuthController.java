package com.example.hrm.controller;

import com.example.hrm.dto.LoginRequest;
import com.example.hrm.dto.RegisterRequest;
import com.example.hrm.entity.User;
import com.example.hrm.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("Public endpoint access granted!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .role("USER")
                .enabled(true)
                .build();

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElse(null);

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "username or password incorrect"));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "user", Map.of("username", user.getUsername(), "fullName", user.getFullName())
        ));
    }
}