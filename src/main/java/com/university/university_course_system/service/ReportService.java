package com.university.university_course_system.service;

import com.university.university_course_system.dto.response.CourseGradeDistributionDTO;
import com.university.university_course_system.dto.response.GradeStatisticsDTO;
import com.university.university_course_system.dto.response.StudentGpaDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ReportService {

    // 学生GPA相关
    StudentGpaDTO getStudentGpa(Integer studentId);

    List<StudentGpaDTO> getTopStudentsByGpa(Integer topN);

    List<StudentGpaDTO> getDepartmentStudentsGpa(Integer departmentId);

    // 课程成绩统计
    List<GradeStatisticsDTO> getCourseGradeStatistics();

    GradeStatisticsDTO getCourseGradeStatisticsByCourse(Integer courseId);

    List<GradeStatisticsDTO> getDepartmentCourseStatistics(Integer departmentId);

    // 成绩分布
    CourseGradeDistributionDTO getCourseGradeDistribution(Integer courseId);

    // 系统统计
    BigDecimal getDepartmentAverageGpa(Integer departmentId);

    Integer getCoursePassRate(Integer courseId);
}
