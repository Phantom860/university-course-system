package com.university.university_course_system.controller;

import com.university.university_course_system.dto.request.EnrollmentRequest;
import com.university.university_course_system.dto.request.GradeUpdateRequest;
import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.dto.response.EnrollmentDetailDTO;
import com.university.university_course_system.dto.response.EnrollmentResponse;
import com.university.university_course_system.service.AuthService;
import com.university.university_course_system.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@Tag(name = "选课管理模块", description = "学生选课、退课、成绩录入等操作")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AuthService authService;

    @Operation(summary = "根据选课ID获取选课记录", description = "通过选课记录ID查询详细的选课信息")
    @GetMapping("/{enrollmentId}")
    public ApiResponse<EnrollmentResponse> getEnrollmentById(@PathVariable Integer enrollmentId) {
        try {
            EnrollmentResponse enrollment = enrollmentService.getEnrollmentById(enrollmentId);
            return ApiResponse.success(enrollment);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "获取学生的所有选课记录", description = "查询指定学生的所有选课记录和成绩信息")
    @GetMapping("/student/{studentId}")
    public ApiResponse<List<EnrollmentResponse>> getEnrollmentsByStudent(@PathVariable Integer studentId) {
        try {
            List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
            return ApiResponse.success(enrollments);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "获取课程段的所有选课记录", description = "查询指定课程段的所有学生选课记录")
    @GetMapping("/section/{sectionId}")
    public ApiResponse<List<EnrollmentDetailDTO>> getEnrollmentsBySectionId(@PathVariable Integer sectionId) {
        List<EnrollmentDetailDTO> enrollments = enrollmentService.getEnrollmentsBySectionId(sectionId);

        if (enrollments == null || enrollments.isEmpty()) {
            // 没有选课记录就返回 error
            return ApiResponse.error("sectionId=" + sectionId + " 没有选课记录");
        }

        return ApiResponse.success(enrollments);
    }

    /*
    学生选课
     */
    @Operation(summary = "学生选课", description = "学生选择指定的课程段进行选课")
    @PostMapping("/enroll")
    public ApiResponse<EnrollmentResponse> enrollCourse(@RequestBody EnrollmentRequest enrollmentRequest, HttpSession session) {

        if (!authService.isStudent(session)) {
            return ApiResponse.error("权限不足，需要学生权限");
        }

        try {
            EnrollmentResponse enrollment = enrollmentService.enrollCourse(enrollmentRequest, session);
            return ApiResponse.success(enrollment);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /*
    退课
     */
    @Operation(summary = "学生退课", description = "学生退选已选的课程")
    @PostMapping("/{enrollmentId}/drop")
    public ApiResponse<EnrollmentResponse> dropCourse(@PathVariable Integer enrollmentId, HttpSession session) {

        if (!authService.isStudent(session)) {
            return ApiResponse.error("权限不足，需要学生权限");
        }

        try {
            EnrollmentResponse enrollment = enrollmentService.dropCourse(enrollmentId, session);
            return ApiResponse.success(enrollment);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /*
    教师录入成绩
     */
    @Operation(summary = "教师录入成绩", description = "教师为学生的选课记录录入或更新成绩")
    @PutMapping("/{enrollmentId}/grade")
    public ApiResponse<EnrollmentResponse> updateGrade(
            @PathVariable Integer enrollmentId,
            @RequestBody GradeUpdateRequest gradeRequest, HttpSession session) {

        if (!authService.isInstructor(session)) {
            return ApiResponse.error("权限不足，需要教师权限");
        }

        try {
            EnrollmentResponse enrollment = enrollmentService.updateGrade(enrollmentId, gradeRequest, session);
            return ApiResponse.success(enrollment);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /*
    删除选课记录
     */
    @Operation(summary = "删除选课记录", description = "删除指定的选课记录（通常用于系统管理）")
    @DeleteMapping("/{enrollmentId}")
    public ApiResponse<Void> deleteEnrollment(@PathVariable Integer enrollmentId) {
        try {
            enrollmentService.deleteEnrollment(enrollmentId);
            return ApiResponse.success(null);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }


}
