// AuthService.java
package com.university.university_course_system.service;

import com.university.university_course_system.dto.request.LoginRequest;
import com.university.university_course_system.dto.request.RegisterRequest;
import com.university.university_course_system.dto.response.AuthResponse;
import com.university.university_course_system.dto.response.RegisterResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    // 用户登录
    AuthResponse login(LoginRequest loginRequest, HttpSession session);

    // 用户注册
    RegisterResponse register(RegisterRequest registerRequest);

    // 用户登出
    void logout(HttpSession session);

    // 获取当前登录用户
    com.university.university_course_system.entity.User getCurrentUser(HttpSession session);

    // 检查是否是管理员
    boolean isAdmin(HttpSession session);

    // 检查是否是学生
    boolean isStudent(HttpSession session);

    // 检查是否是教师
    boolean isInstructor(HttpSession session);
}