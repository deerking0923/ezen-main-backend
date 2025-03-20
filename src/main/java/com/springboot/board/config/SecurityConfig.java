package com.springboot.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.board.security.JwtAuthenticationFilter;
import com.springboot.board.security.JwtTokenProvider;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider provider) {
        return new JwtAuthenticationFilter(provider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            .csrf().disable()
            .cors()
            .and()
            .authorizeHttpRequests()
                // 인증 없이 접근할 수 있는 엔드포인트 설정 (로그인, 회원가입) 일단.
                .requestMatchers("/api/v1/auth/**").permitAll()
                // 일반 GET 요청은 공개
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                // 마이 라이브러리 서비스는 인증 필요
                .requestMatchers("/mylibrary-service/**").authenticated()
                // 그 외의 모든 요청은 인증 필요
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    
}
