package com.university.university_course_system.controller;

import com.university.university_course_system.dto.response.InstructorDTO;
import com.university.university_course_system.dto.request.InstructorRequest;
import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.service.AuthService;
import com.university.university_course_system.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/instructors")
@Tag(name = "教师管理模块", description = "教师信息的查询、创建、更新和删除操作")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private AuthService authService;

    // 获取所有教师
    @Operation(summary = "获取所有教师列表", description = "获取系统中所有教师的基本信息列表")
    @GetMapping
    public ApiResponse<List<InstructorDTO>> getAllInstructors(HttpSession session) {

        List<InstructorDTO> instructors = instructorService.getAllInstructors();
        return ApiResponse.success(instructors);
    }

    // 根据ID获取教师信息
    @Operation(summary = "根据ID获取教师信息", description = "通过教师ID查询教师的详细信息")
    @GetMapping("/{instructorId}")
    public ApiResponse<InstructorDTO> getInstructorById(@PathVariable Integer instructorId, HttpSession session) {

        InstructorDTO instructor = instructorService.getInstructorById(instructorId);
        if (instructor != null) {
            return ApiResponse.success(instructor);
        } else {
            return ApiResponse.error("教师不存在");
        }
    }

    // 获取当前登录教师的信息
    @Operation(summary = "获取当前登录教师信息", description = "获取当前已登录教师的个人信息")
    @GetMapping("/my-profile")
    public ApiResponse<InstructorDTO> getMyProfile(HttpSession session) {
        if (!authService.isInstructor(session)) {
            return ApiResponse.error("权限不足，需要教师权限");
        }

        com.university.university_course_system.entity.User currentUser = authService.getCurrentUser(session);
        InstructorDTO instructor = instructorService.getInstructorByUserId(currentUser.getUserId());
        if (instructor != null) {
            return ApiResponse.success(instructor);
        } else {
            return ApiResponse.error("教师信息未找到");
        }
    }

    // 创建教师（需要管理员权限）
    @Operation(summary = "创建新教师", description = "管理员创建新的教师账号")
    @PostMapping
    public ApiResponse<String> createInstructor(@RequestBody InstructorRequest request, HttpSession session) {
        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        boolean result = instructorService.createInstructor(request);
        if (result) {
            return ApiResponse.success("教师创建成功");
        } else {
            return ApiResponse.error("教师创建失败");
        }
    }

    // 更新教师信息
    @Operation(summary = "更新教师信息", description = "更新指定教师的信息（管理员可更新所有教师，教师只能更新自己的信息）")
    @PutMapping("/{instructorId}")
    public ApiResponse<String> updateInstructor(@PathVariable Integer instructorId,
                                                @RequestBody InstructorRequest request,
                                                HttpSession session) {
        if (!authService.isAdmin(session)) {
            // 教师只能更新自己的信息
            if (authService.isInstructor(session)) {
                com.university.university_course_system.entity.User currentUser = authService.getCurrentUser(session);
                InstructorDTO currentInstructor = instructorService.getInstructorByUserId(currentUser.getUserId());
                if (currentInstructor == null || !currentInstructor.getInstructorId().equals(instructorId)) {
                    return ApiResponse.error("只能修改自己的信息");
                }
            } else {
                return ApiResponse.error("权限不足");
            }
        }

        boolean result = instructorService.updateInstructor(instructorId, request);
        if (result) {
            return ApiResponse.success("教师信息更新成功");
        } else {
            return ApiResponse.error("教师信息更新失败");
        }
    }

    // 删除教师（需要管理员权限）
    @Operation(summary = "删除教师", description = "删除指定教师账号（仅管理员可操作）")
    @DeleteMapping("/{instructorId}")
    public ApiResponse<String> deleteInstructor(@PathVariable Integer instructorId, HttpSession session) {
        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        boolean result = instructorService.deleteInstructor(instructorId);
        if (result) {
            return ApiResponse.success("教师删除成功");
        } else {
            return ApiResponse.error("教师删除失败");
        }
    }

    // 根据部门获取教师
    @Operation(summary = "根据部门获取教师列表", description = "获取指定部门下的所有教师列表（仅管理员可操作）")
    @GetMapping("/department/{departmentId}")
    public ApiResponse<List<InstructorDTO>> getInstructorsByDepartment(@PathVariable Integer departmentId, HttpSession session) {
        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        List<InstructorDTO> instructors = instructorService.getInstructorsByDepartment(departmentId);
        return ApiResponse.success(instructors);
    }

    // 搜索教师
    @Operation(summary = "搜索教师", description = "根据关键词搜索教师（仅管理员可操作）")
    @GetMapping("/search")
    public ApiResponse<List<InstructorDTO>> searchInstructors(@RequestParam String keyword, HttpSession session) {
        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        List<InstructorDTO> instructors = instructorService.searchInstructors(keyword);
        return ApiResponse.success(instructors);
    }
}