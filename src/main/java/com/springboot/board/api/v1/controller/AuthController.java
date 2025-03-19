package com.springboot.board.api.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.springboot.board.domain.entity.User;
import com.springboot.board.domain.repository.UserRepository;
import com.springboot.board.security.JwtTokenProvider;
import com.springboot.board.api.v1.dto.request.AuthRequest;
import com.springboot.board.api.v1.dto.response.AuthResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody AuthRequest req) {
        User user = User.builder().username(req.getUsername()).password(req.getPassword()).email(req.getEmail()).build();
        user.encodePassword(encoder);
        user.getRoles().add("ROLE_USER");
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) throw new RuntimeException("Invalid credentials");
        String token = jwtProvider.createToken(user.getUsername(), user.getRoles());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}