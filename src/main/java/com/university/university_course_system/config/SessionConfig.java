package com.university.university_course_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpSession;

@Configuration
public class SessionConfig implements WebMvcConfigurer {

    // 允许跨域请求（前端测试时需要）
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // ⭐ 允许任意前端域名
                .allowedMethods("*")        // ⭐ 允许所有方法 GET POST PUT DELETE
                .allowedHeaders("*")        // ⭐ 允许所有请求头
                .allowCredentials(true)     // ⭐ 允许携带 cookie

                .maxAge(3600);
    }
}
