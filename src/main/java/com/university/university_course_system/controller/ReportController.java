package com.university.university_course_system.controller;

import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.dto.response.CourseGradeDistributionDTO;
import com.university.university_course_system.dto.response.GradeStatisticsDTO;
import com.university.university_course_system.dto.response.StudentGpaDTO;
import com.university.university_course_system.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "统计报表模块", description = "成绩统计、GPA计算、课程分析等报表功能")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // 学生GPA统计
    @Operation(summary = "获取学生GPA统计", description = "查询指定学生的GPA信息和成绩统计")
    @GetMapping("/students/{studentId}/gpa")
    public ApiResponse<StudentGpaDTO> getStudentGpa(@PathVariable Integer studentId) {
        try {
            StudentGpaDTO gpa = reportService.getStudentGpa(studentId);
            return ApiResponse.success(gpa);
        } catch (Exception e) {
            return ApiResponse.error("获取GPA失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取GPA排名前N的学生", description = "获取全校GPA排名前N的优秀学生列表")
    @GetMapping("/students/top/{topN}")
    public ApiResponse<List<StudentGpaDTO>> getTopStudentsByGpa(@PathVariable Integer topN) {
        try {
            List<StudentGpaDTO> topStudents = reportService.getTopStudentsByGpa(topN);
            return ApiResponse.success(topStudents);
        } catch (Exception e) {
            return ApiResponse.error("获取优秀学生失败: " + e.getMessage());
        }
    }

    // 课程成绩统计
    @Operation(summary = "获取所有课程成绩统计", description = "获取所有课程的成绩统计信息（平均分、最高分、最低分等）")
    @GetMapping("/courses/statistics")
    public ApiResponse<List<GradeStatisticsDTO>> getCourseGradeStatistics() {
        try {
            List<GradeStatisticsDTO> statistics = reportService.getCourseGradeStatistics();
            return ApiResponse.success(statistics);
        } catch (Exception e) {
            return ApiResponse.error("获取课程统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取指定课程成绩统计", description = "获取单个课程的详细成绩统计信息")
    @GetMapping("/courses/{courseId}/statistics")
    public ApiResponse<GradeStatisticsDTO> getCourseStatistics(@PathVariable Integer courseId) {
        try {
            GradeStatisticsDTO statistics = reportService.getCourseGradeStatisticsByCourse(courseId);
            return ApiResponse.success(statistics);
        } catch (Exception e) {
            return ApiResponse.error("获取课程统计失败: " + e.getMessage());
        }
    }

    // 成绩分布
    @Operation(summary = "获取课程成绩分布", description = "获取指定课程的成绩等级分布情况")
    @GetMapping("/courses/{courseId}/distribution")
    public ApiResponse<CourseGradeDistributionDTO> getCourseGradeDistribution(@PathVariable Integer courseId) {
        try {
            CourseGradeDistributionDTO distribution = reportService.getCourseGradeDistribution(courseId);
            return ApiResponse.success(distribution);
        } catch (Exception e) {
            return ApiResponse.error("获取成绩分布失败: " + e.getMessage());
        }
    }

    // 院系统计
    @Operation(summary = "获取院系学生GPA列表", description = "获取指定院系所有学生的GPA信息")
    @GetMapping("/departments/{departmentId}/gpa")
    public ApiResponse<List<StudentGpaDTO>> getDepartmentStudentsGpa(@PathVariable Integer departmentId) {
        try {
            List<StudentGpaDTO> students = reportService.getDepartmentStudentsGpa(departmentId);
            return ApiResponse.success(students);
        } catch (Exception e) {
            return ApiResponse.error("获取院系学生GPA失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取院系课程统计", description = "获取指定院系所有课程的成绩统计信息")
    @GetMapping("/departments/{departmentId}/courses/statistics")
    public ApiResponse<List<GradeStatisticsDTO>> getDepartmentCourseStatistics(@PathVariable Integer departmentId) {
        try {
            List<GradeStatisticsDTO> statistics = reportService.getDepartmentCourseStatistics(departmentId);
            return ApiResponse.success(statistics);
        } catch (Exception e) {
            return ApiResponse.error("获取院系课程统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取院系平均GPA", description = "计算指定院系的平均GPA")
    @GetMapping("/departments/{departmentId}/average-gpa")
    public ApiResponse<BigDecimal> getDepartmentAverageGpa(@PathVariable Integer departmentId) {
        try {
            BigDecimal averageGpa = reportService.getDepartmentAverageGpa(departmentId);
            return ApiResponse.success(averageGpa);
        } catch (Exception e) {
            return ApiResponse.error("获取院系平均GPA失败: " + e.getMessage());
        }
    }

    //获取课程通过率
    @Operation(summary = "获取课程通过率", description = "计算指定课程的通过率（及格率）")
    @GetMapping("/courses/{courseId}/pass-rate")
    public ApiResponse<Integer> getCoursePassRate(@PathVariable Integer courseId) {
        try {
            Integer passRate = reportService.getCoursePassRate(courseId);
            return ApiResponse.success(passRate);
        } catch (Exception e) {
            return ApiResponse.error("获取课程通过率失败: " + e.getMessage());
        }
    }
}
