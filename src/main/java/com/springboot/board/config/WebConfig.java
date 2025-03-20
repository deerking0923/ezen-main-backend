package com.springboot.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

/**
 * WebConfig 클래스는 CORS 설정을 위한 구성 클래스
 * 이 클래스는 WebMvcConfigurer 인터페이스를 구현하여
 * 웹 애플리케이션의 CORS 정책을 정의
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORS 매핑을 추가하는 메서드
     * 
     * @param registry CORS 설정을 위한 CorsRegistry 객체
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://43.201.239.97:3000",
                    "http://43.201.239.97:8080",
                    "http://localhost:3000",
                    "http://localhost:8080",
                    "http://dadoklog.com",
                    "http://www.dadoklog.com",
                    "https://dadoklog.com",        // 추가
                    "https://www.dadoklog.com"       // 추가
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
}