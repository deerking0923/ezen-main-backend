package com.springboot.board.api.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.springboot.board.domain.entity.User;
import com.springboot.board.domain.repository.UserRepository;
import com.springboot.board.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.springboot.board.api.v1.dto.request.AuthRequest;
import com.springboot.board.api.v1.dto.response.AuthResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody AuthRequest req) {
        User user = User.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .email(req.getEmail())
                .build();
        user.encodePassword(encoder);
        user.getRoles().add("ROLE_USER");
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthRequest req, HttpServletResponse response) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtProvider.createToken(user.getUsername(), user.getRoles());
        
        // JWT 토큰을 HttpOnly 쿠키에 저장
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int)(jwtProvider.getValidityMs() / 1000)); // 토큰 유효기간(초)
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
            !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // JwtAuthenticationFilter에서 principal에 username을 설정한 상태라고 가정
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("roles", user.getRoles());
        return ResponseEntity.ok(userInfo);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // 동일한 이름의 쿠키를 만료시켜 삭제
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
