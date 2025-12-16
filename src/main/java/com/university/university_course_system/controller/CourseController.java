package com.university.university_course_system.controller;

import com.university.university_course_system.dto.request.CourseRequest;
import com.university.university_course_system.dto.response.ApiResponse;
import com.university.university_course_system.dto.response.CourseResponse;
import com.university.university_course_system.dto.response.CourseSectionDetailDTO;
import com.university.university_course_system.entity.CourseSection;
import com.university.university_course_system.mapper.CourseSectionMapper;
import com.university.university_course_system.service.AuthService;
import com.university.university_course_system.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Tag(name = "课程管理模块", description = "课程信息的增删改查操作")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Autowired
    private AuthService authService;

    @Operation(summary = "根据ID获取课程信息", description = "通过课程ID查询课程详细信息")
    @GetMapping("/{courseId}")
    public ApiResponse<CourseResponse> getCourseById(@PathVariable Integer courseId) {
        try {
            CourseResponse course = courseService.getCourseById(courseId);
            return ApiResponse.success(course);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "根据课程代码获取课程信息", description = "通过课程代码查询课程详细信息")
    @GetMapping("/code/{courseCode}")
    public ApiResponse<CourseResponse> getCourseByCode(@PathVariable String courseCode) {
        try {
            CourseResponse course = courseService.getCourseByCode(courseCode);
            return ApiResponse.success(course);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "获取所有课程列表", description = "获取系统中所有课程的信息列表")
    @GetMapping
    public ApiResponse<List<CourseResponse>> getAllCourses() {
        try {
            List<CourseResponse> courses = courseService.getAllCourses();
            return ApiResponse.success(courses);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "根据院系获取课程列表", description = "获取指定院系下的所有课程")
    @GetMapping("/department/{departmentId}")
    public ApiResponse<List<CourseResponse>> getCoursesByDepartment(@PathVariable Integer departmentId) {
        try {
            List<CourseResponse> courses = courseService.getCoursesByDepartment(departmentId);
            return ApiResponse.success(courses);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "创建新课程", description = "管理员创建新的课程")
    @PostMapping
    public ApiResponse<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest, HttpSession session) {

        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        try {
            CourseResponse course = courseService.createCourse(courseRequest, session);
            return ApiResponse.success(course);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "更新课程信息", description = "更新指定课程的信息（仅管理员可操作）")
    @PutMapping("/{courseId}")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable Integer courseId,
            @RequestBody CourseRequest courseRequest, HttpSession session) {

        if (!authService.isAdmin(session)) {
            return ApiResponse.error("权限不足，需要管理员权限");
        }

        try {
            CourseResponse course = courseService.updateCourse(courseId, courseRequest, session);
            return ApiResponse.success(course);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "删除课程", description = "删除指定课程（仅管理员可操作）")
    @DeleteMapping("/{courseId}")
    public ApiResponse<Void> deleteCourse(@PathVariable Integer courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ApiResponse.success(null);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }


    /*
    根据coursename获取排课信息
     */
    @GetMapping("/sections")
    public ApiResponse<List<CourseSection>> getSectionsByCourseName(
            @RequestParam String courseName) {

        try {
            List<CourseSection> sections = courseService.getSectionsByCourseName(courseName);

            if (sections == null || sections.isEmpty()) {
                return ApiResponse.error("未找到名称为 '" + courseName + "' 的课程或无排课信息");
            }

            return ApiResponse.success(sections);

        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }



    /*
    根据教师id查询排课信息
     */
    @GetMapping("/instructor/{instructorId}")
    public ApiResponse<List<CourseSection>> getSectionsByInstructorId(
            @PathVariable Integer instructorId) {

        try {
            List<CourseSection> sections = courseService.getSectionsByInstructor(instructorId);

            if (sections == null || sections.isEmpty()) {
                return ApiResponse.error("该教师暂无排课信息");
            }

            return ApiResponse.success(sections);

        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }


    /*
    根据sectionid查询课程相关信息
     */
    @GetMapping("/sections/detail")
    public ApiResponse<CourseSectionDetailDTO> getSectionDetail(@RequestParam Integer sectionId) {
        CourseSectionDetailDTO sectionDetail = courseService.getSectionDetail(sectionId);
        if (sectionDetail == null) {
            // 查询不到，直接返回错误信息，不抛异常
            return ApiResponse.error("sectionId=" + sectionId + " 的排课不存在");
        }

        // 查询到，返回成功
        return ApiResponse.success(sectionDetail);
    }

}