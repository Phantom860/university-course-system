package com.university.university_course_system.service;

import com.university.university_course_system.dto.request.StudentRequest;
import com.university.university_course_system.dto.response.StudentResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface StudentService {

    StudentResponse getStudentById(Integer studentId);

    StudentResponse getStudentByNumber(String studentNumber);

    List<StudentResponse> getAllStudents();

    StudentResponse createStudent(StudentRequest studentRequest, HttpSession session);

    StudentResponse updateStudent(Integer studentId, StudentRequest studentRequest, HttpSession session);

    void deleteStudent(Integer studentId, HttpSession session);

    boolean existsByStudentNumber(String studentNumber);
}
