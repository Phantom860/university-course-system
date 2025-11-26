package com.university.university_course_system.controller;

import com.university.university_course_system.dto.request.StudentRequest;
import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.dto.response.StudentResponse;
import com.university.university_course_system.service.AuthService;
import com.university.university_course_system.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@Tag(name = "学生管理模块", description = "学生信息的增删改查操作")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthService authService;

    @Operation(summary = "根据ID获取学生信息", description = "通过学生ID查询学生详细信息")
    @GetMapping("/{studentId}")
    public ApiResponse<StudentResponse> getStudentById(@PathVariable Integer studentId) {
        try {
            StudentResponse student = studentService.getStudentById(studentId);
            return ApiResponse.success(student);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "根据学号获取学生信息", description = "通过学号查询学生详细信息")
    @GetMapping("/number/{studentNumber}")
    public ApiResponse<StudentResponse> getStudentByNumber(@PathVariable String studentNumber) {
        try {
            StudentResponse student = studentService.getStudentByNumber(studentNumber);
            return ApiResponse.success(student);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "获取所有学生列表", description = "获取系统中所有学生的信息列表")
    @GetMapping
    public ApiResponse<List<StudentResponse>> getAllStudents() {
        try {
            List<StudentResponse> students = studentService.getAllStudents();
            return ApiResponse.success(students);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }


    @Operation(summary = "创建新学生", description = "管理员创建新的学生账号")
    @PostMapping
    public ApiResponse<StudentResponse> createStudent(@RequestBody StudentRequest studentRequest, HttpSession session) {

        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        try {
            StudentResponse student = studentService.createStudent(studentRequest, session);
            return ApiResponse.success(student);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }


    @Operation(summary = "更新学生信息", description = "更新指定学生的信息（管理员或学生本人可操作）")
    @PutMapping("/{studentId}")
    public ApiResponse<StudentResponse> updateStudent(
            @PathVariable Integer studentId,
            @RequestBody StudentRequest studentRequest, HttpSession session) {

        if (authService.isInstructor(session)) {
            return ApiResponse.error("权限不足，需要管理员或学生权限");
        }

        try {
            StudentResponse student = studentService.updateStudent(studentId, studentRequest, session);
            return ApiResponse.success(student);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }


    @Operation(summary = "删除学生", description = "删除指定学生账号（仅管理员可操作）")
    @DeleteMapping("/{studentId}")
    public ApiResponse<Void> deleteStudent(@PathVariable Integer studentId, HttpSession session) {

        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        try {
            studentService.deleteStudent(studentId, session);
            return ApiResponse.success(null);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
