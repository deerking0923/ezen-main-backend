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
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                // /api/v1/** (예: 게시글 조회 등)는 GET 요청은 공개
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                // 하지만 /mylibrary-service/**는 GET이라도 인증 필요 (개인 데이터 조회)
                .requestMatchers("/mylibrary-service/**").authenticated()
                // 나머지 POST, PUT, DELETE 등은 인증 필요
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
