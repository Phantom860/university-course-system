package com.university.university_course_system.service;

import com.university.university_course_system.entity.CourseSection;

import java.util.List;

public interface CourseScheduleService {
    boolean hasTimeConflict(CourseSection section1, CourseSection section2);
    boolean hasTimeConflictWithEnrolledCourses(Integer studentId, CourseSection newSection);
    List<CourseSection> getEnrolledSectionsByStudentAndSemesterId(Integer studentId, Integer semesterId);
}