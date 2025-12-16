package com.university.university_course_system.controller;

import com.university.university_course_system.dto.request.LoginRequest;
import com.university.university_course_system.dto.request.RegisterRequest;
import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.dto.response.AuthResponse;
import com.university.university_course_system.dto.response.IdResponse;
import com.university.university_course_system.dto.response.RegisterResponse;
import com.university.university_course_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证模块", description = "用户登录、注册、登出等认证相关操作")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 根据userId获取学生ID
     */
    @GetMapping("/student-id/{userId}")
    public ApiResponse<IdResponse> getStudentId(@PathVariable Integer userId) {
        try {
            Integer studentId = authService.getStudentIdByUserId(userId);
            if (studentId != null) {
                IdResponse idResponse = new IdResponse(studentId, "student");
                return ApiResponse.success(idResponse);
            } else {
                return ApiResponse.error("该用户不是学生");
            }
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }


    /**
     * 根据userId获取教师ID
     */
    @GetMapping("/instructor-id/{userId}")
    public ApiResponse<IdResponse> getInstructorId(@PathVariable Integer userId) {
        try {
            Integer instructorId = authService.getInstructorIdByUserId(userId);
            if (instructorId != null) {
                IdResponse idResponse = new IdResponse(instructorId, "instructor");
                return ApiResponse.success(idResponse);
            } else {
                return ApiResponse.error("该用户不是教师");
            }
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }


    @Operation(summary = "用户登录", description = "使用用户名和密码登录系统，成功后建立Session")
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        System.out.println("=== 登录请求到达Controller ===");
        System.out.println("用户名: " + loginRequest.getUsername());
        AuthResponse authResponse = authService.login(loginRequest, session);

        if (authResponse.getToken() != null) {
            return ApiResponse.success(authResponse);
        } else {
            return ApiResponse.error(authResponse.getMessage());
        }
    }


    @Operation(summary = "用户注册", description = "新用户注册账号，注册后需要管理员审核")
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.register(registerRequest);

        if (registerResponse.isSuccess()) {
            return ApiResponse.success(registerResponse);
        } else {
            return ApiResponse.error(registerResponse.getMessage());
        }
    }


    @Operation(summary = "用户登出", description = "退出登录，清除Session信息")
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpSession session) {
        authService.logout(session);
        return ApiResponse.success("登出成功");
    }


    @Operation(summary = "获取当前用户信息", description = "获取当前已登录用户的详细信息")
    @GetMapping("/current-user")
    public ApiResponse<com.university.university_course_system.entity.User> getCurrentUser(HttpSession session) {
        com.university.university_course_system.entity.User currentUser = authService.getCurrentUser(session);
        if (currentUser != null) {
            return ApiResponse.success(currentUser);
        } else {
            return ApiResponse.error("用户未登录");
        }
    }


    @Operation(summary = "认证模块测试", description = "测试认证模块是否正常工作")
    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.success("认证模块测试成功");
    }


    @PutMapping("/{userId}/contact")
    public ApiResponse<Void> updateContact(@PathVariable Integer userId,
                                           @RequestParam String phone,
                                           @RequestParam String email) {
        boolean success = authService.updateContactInfo(userId, phone, email);
        if (success) {
            return ApiResponse.success(null); // 修改成功，无返回数据
        } else {
            return ApiResponse.error("用户不存在或更新失败");
        }
    }
}