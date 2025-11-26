package com.university.university_course_system.service;

import com.university.university_course_system.dto.request.EnrollmentRequest;
import com.university.university_course_system.dto.request.GradeUpdateRequest;
import com.university.university_course_system.dto.response.EnrollmentResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse getEnrollmentById(Integer enrollmentId);

    List<EnrollmentResponse> getEnrollmentsByStudent(Integer studentId);

    List<EnrollmentResponse> getEnrollmentsBySection(Integer sectionId);

    EnrollmentResponse enrollCourse(EnrollmentRequest enrollmentRequest, HttpSession session);

    EnrollmentResponse dropCourse(Integer enrollmentId, HttpSession session);

    EnrollmentResponse updateGrade(Integer enrollmentId, GradeUpdateRequest gradeRequest, HttpSession session);

    void deleteEnrollment(Integer enrollmentId);

    boolean checkPrerequisites(Integer studentId, Integer sectionId);
}
