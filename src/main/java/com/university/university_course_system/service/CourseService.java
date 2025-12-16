package com.university.university_course_system.service;

import com.university.university_course_system.dto.request.CourseRequest;
import com.university.university_course_system.dto.response.CourseResponse;
import com.university.university_course_system.dto.response.CourseSectionDetailDTO;
import com.university.university_course_system.entity.CourseSection;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface CourseService {

    CourseResponse getCourseById(Integer courseId);

    CourseResponse getCourseByCode(String courseCode);

    List<CourseResponse> getAllCourses();

    List<CourseResponse> getCoursesByDepartment(Integer departmentId);

    CourseResponse createCourse(CourseRequest courseRequest, HttpSession session);

    CourseResponse updateCourse(Integer courseId, CourseRequest courseRequest, HttpSession session);

    void deleteCourse(Integer courseId);

    boolean existsByCourseCode(String courseCode);

    public List<CourseSection> getSectionsByCourseName(String courseName);

    public List<CourseSection> getSectionsByInstructor(Integer instructorId);

    public CourseSectionDetailDTO getSectionDetail(Integer sectionId);
}
