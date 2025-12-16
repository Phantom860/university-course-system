// AdminController.java
package com.university.university_course_system.controller;

import com.university.university_course_system.dto.UserInfoDTO;
import com.university.university_course_system.dto.request.ApprovalRequest;
import com.university.university_course_system.dto.request.PrereqSettingRequest;
import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.dto.response.ApprovalResponse;
import com.university.university_course_system.entity.CoursePrereq;
import com.university.university_course_system.entity.CourseSection;
import com.university.university_course_system.entity.User;
import com.university.university_course_system.service.AdminService;
import com.university.university_course_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/admin")
@Tag(name = "管理员模块", description = "管理员专用接口，包括用户审核、课程管理等")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;

    // 权限检查方法
    private boolean checkAdminPermission(HttpSession session) {
        User currentUser = authService.getCurrentUser(session);
        if (currentUser == null) {
            System.out.println("权限检查: 用户未登录");
            return false;
        }

        boolean isAdmin = currentUser.getUserType() == User.UserType.admin;
        System.out.println("权限检查: 用户 " + currentUser.getUsername() +
                ", 类型: " + currentUser.getUserType() +
                ", 是管理员: " + isAdmin);
        return isAdmin;
    }


    @Operation(summary = "获取待审核用户列表")
    // 1. 获取待审核用户列表
    @GetMapping("/pending-users")
    public ApiResponse<List<UserInfoDTO>> getPendingUsers(HttpSession session) {
        System.out.println("获取待审核用户列表");

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        List<UserInfoDTO> pendingUsers = adminService.getPendingUsers();
        return ApiResponse.success(pendingUsers);
    }


    @Operation(summary = "审核单个用户")
    // 2. 审核用户
    @PostMapping("/approve-user")
    public ApiResponse<ApprovalResponse> approveUser(@RequestBody ApprovalRequest request, HttpSession session) {
        System.out.println("审核用户请求: " + request.getUserId() + " - " + request.getAction());

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        ApprovalResponse response = adminService.approveUser(request);
        if (response.isSuccess()) {
            return ApiResponse.success(response);
        } else {
            return ApiResponse.error(response.getMessage());
        }
    }


    @Operation(summary = "批量审核用户")
    // 3. 批量审核用户
    @PostMapping("/batch-approve")
    public ApiResponse<ApprovalResponse> batchApproveUsers(@RequestBody List<Integer> userIds, HttpSession session) {
        System.out.println("批量审核用户: " + userIds);

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        ApprovalResponse response = adminService.batchApproveUsers(userIds);
        if (response.isSuccess()) {
            return ApiResponse.success(response);
        } else {
            return ApiResponse.error(response.getMessage());
        }
    }


    @Operation(summary = "获取用户列表")
    // 4. 获取用户列表（可按状态筛选）
    @GetMapping("/users")
    public ApiResponse<List<UserInfoDTO>> getUsersByStatus(
            @RequestParam(required = false) String status,
            HttpSession session) {

        System.out.println("获取用户列表，状态: " + status);

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        List<UserInfoDTO> users;
        if (status != null && !status.trim().isEmpty()) {
            users = adminService.getUsersByStatus(status);
        } else {
            users = adminService.getAllUsers();
        }
        return ApiResponse.success(users);
    }


    @Operation(summary = "获取用户统计信息")
    // 5. 获取用户统计信息
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getUserStats(HttpSession session) {
        System.out.println("获取用户统计信息");

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        Map<String, Object> stats = adminService.getUserStats();
        return ApiResponse.success(stats);
    }


    @Operation(summary = "管理员测试接口")
    // 6. 管理员测试接口
    @GetMapping("/test")
    public ApiResponse<String> test(HttpSession session) {
        System.out.println("管理员测试接口");

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        User currentUser = authService.getCurrentUser(session);
        return ApiResponse.success("管理员接口测试成功 - 当前用户: " + currentUser.getUsername());
    }


    @Operation(summary = "设置课程段最大容量")
    /**
     * 设置课程段最大容量
     */
    @PutMapping("/{sectionId}/capacity")
    public ApiResponse<String> setMaxCapacity(@PathVariable Integer sectionId,
                                      @RequestParam Integer maxCapacity, HttpSession session) {

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        try {
            boolean success = adminService.setMaxCapacity(sectionId, maxCapacity);
            return ApiResponse.success("容量设置成功");
        } catch (Exception e) {
            return ApiResponse.error("容量设置失败: " + e.getMessage());
        }
    }

    /**
     * 更新课程段信息
     */
    @Operation(summary = "更新课程段信息")
    @PutMapping("/{sectionId}")
    public ApiResponse<String> updateCourseSection(@PathVariable Integer sectionId,
                                           @RequestBody CourseSection courseSection, HttpSession session) {

        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }
        try {
            // 确保URL中的sectionId与请求体中的一致
            courseSection.setSectionId(sectionId);

            boolean success = adminService.updateCourseSection(courseSection);
            return ApiResponse.success("课程段信息更新成功");
        } catch (Exception e) {
            return ApiResponse.error("课程段信息更新失败: " + e.getMessage());
        }
    }


    /*
    设置先修课程
     */
    @PostMapping("/course/setPrereq")
    public ApiResponse<String> setPrerequisite(@RequestBody PrereqSettingRequest request,
                                               HttpSession session) {
        if (!checkAdminPermission(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        adminService.setCoursePrerequisites(request);
        return ApiResponse.success("先修课程设置成功");
    }


    /*
    查看先修课程信息
     */
    @GetMapping("/course/getPrereq/{courseId}")
    public ApiResponse<List<CoursePrereq>> getPrerequisite(@PathVariable Integer courseId,
                                                           HttpSession session) {
        return ApiResponse.success(adminService.getCoursePrerequisites(courseId));
    }


}