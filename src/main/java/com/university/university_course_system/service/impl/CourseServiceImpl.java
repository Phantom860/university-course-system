package com.university.university_course_system.service.impl;

import com.university.university_course_system.dto.request.CourseRequest;
import com.university.university_course_system.dto.response.CourseResponse;
import com.university.university_course_system.entity.Course;
import com.university.university_course_system.mapper.CourseMapper;
import com.university.university_course_system.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public CourseResponse getCourseById(Integer courseId) {
        Course course = courseMapper.findByIdWithDepartment(courseId);
        return convertToResponse(course);
    }

    @Override
    public CourseResponse getCourseByCode(String courseCode) {
        Course course = courseMapper.findByCourseCode(courseCode);
        return convertToResponse(course);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseMapper.findAllWithDepartment();
        return courses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesByDepartment(Integer departmentId) {
        List<Course> courses = courseMapper.findByDepartmentId(departmentId);
        return courses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse createCourse(CourseRequest courseRequest, HttpSession session) {
        // 检查课程代码是否已存在
        if (existsByCourseCode(courseRequest.getCourseCode())) {
            throw new RuntimeException("课程代码已存在: " + courseRequest.getCourseCode());
        }

        Course course = new Course();
        BeanUtils.copyProperties(courseRequest, course);

        courseMapper.insert(course);
        return getCourseById(course.getCourseId());
    }

    @Override
    public CourseResponse updateCourse(Integer courseId, CourseRequest courseRequest, HttpSession session) {
        Course existingCourse = courseMapper.findByIdWithDepartment(courseId);
        if (existingCourse == null) {
            throw new RuntimeException("课程不存在，ID: " + courseId);
        }

        // 检查课程代码是否被其他课程使用
        Course courseWithSameCode = courseMapper.findByCourseCode(courseRequest.getCourseCode());
        if (courseWithSameCode != null && !courseWithSameCode.getCourseId().equals(courseId)) {
            throw new RuntimeException("课程代码已被其他课程使用: " + courseRequest.getCourseCode());
        }

        Course course = new Course();
        BeanUtils.copyProperties(courseRequest, course);
        course.setCourseId(courseId);

        courseMapper.update(course);
        return getCourseById(courseId);
    }

    @Override
    public void deleteCourse(Integer courseId) {
        Course course = courseMapper.findByIdWithDepartment(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在，ID: " + courseId);
        }
        courseMapper.delete(courseId);
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        return courseMapper.countByCourseCode(courseCode) > 0;
    }

    private CourseResponse convertToResponse(Course course) {
        if (course == null) {
            return null;
        }

        CourseResponse response = new CourseResponse();
        BeanUtils.copyProperties(course, response);

        // 设置关联的院系名称
        if (course.getDepartment() != null) {
            response.setDepartmentName(course.getDepartment().getDepartmentName());
        }

        return response;
    }
}
