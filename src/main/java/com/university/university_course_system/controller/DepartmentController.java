package com.university.university_course_system.controller;

import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.entity.Department;
import com.university.university_course_system.service.AuthService;
import com.university.university_course_system.service.DepartmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ApiResponse<Department> addDepartment(@RequestBody Department department, HttpSession session) {
        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }
        boolean success = departmentService.addDepartment(department);
        return success ? ApiResponse.success(department) : ApiResponse.error("添加失败");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDepartment(@PathVariable("id") Integer id, HttpSession session) {
        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }
        boolean success = departmentService.deleteDepartment(id);
        return success ? ApiResponse.success(null) : ApiResponse.error("删除失败或学院不存在");
    }

    @PutMapping
    public ApiResponse<Department> updateDepartment(@RequestBody Department department, HttpSession session) {
        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }
        boolean success = departmentService.updateDepartment(department);
        return success ? ApiResponse.success(department) : ApiResponse.error("更新失败或学院不存在");
    }

    @GetMapping("/{id}")
    public ApiResponse<Department> getDepartmentById(@PathVariable("id") Integer id) {
        Department department = departmentService.getDepartmentById(id);
        return department != null ? ApiResponse.success(department) : ApiResponse.error("学院不存在");
    }

    @GetMapping
    public ApiResponse<List<Department>> getAllDepartments() {
        List<Department> list = departmentService.getAllDepartments();
        return ApiResponse.success(list);
    }
}


