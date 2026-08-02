package com.example.hrm.controller;

import com.example.hrm.dto.*;
import com.example.hrm.entity.User;
import com.example.hrm.repository.UserRepository;
import com.example.hrm.security.JwtProvider;
import com.example.hrm.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .role("USER")
                .enabled(false)
                .otpCode(otp)
                .otpExpiration(LocalDateTime.now().plusMinutes(5))
                .build();

        userRepository.save(user);
        mailService.sendOtpEmail(user.getEmail(), otp);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Đăng ký thành công. Vui lòng kiểm tra email kích hoạt"));
    }

    @PostMapping("/active-user")
    public ResponseEntity<?> activeUser(@Valid @RequestBody ActiveRequest dto) {
        User user = userRepository.findAll().stream()
                .filter(u -> dto.getEmail().equals(u.getEmail()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Không tìm thấy tài khoản với email này"));
        }

        if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP đã hết hạn"));
        }

        if (!user.getOtpCode().equals(dto.getOtp())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP nhập vào không chính xác"));
        }

        user.setEnabled(true);
        user.setOtpCode(null);
        user.setOtpExpiration(null);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Kích hoạt thành công"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(dto.getUsername()).orElseThrow();
            String token = jwtProvider.generateToken(userDetails, user.getRole());
            return ResponseEntity.ok(Map.of("accessToken", token, "type", "Bearer", "username", user.getUsername()));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Tài khoản chưa được kích hoạt"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "username or password incorrect"));
        }
    }
}