// InstructorService.java
package com.university.university_course_system.service;

import com.university.university_course_system.dto.response.InstructorDTO;
import com.university.university_course_system.dto.request.InstructorRequest;
import com.university.university_course_system.entity.Instructor;

import java.util.List;

public interface InstructorService {

    // 获取所有教师
    List<InstructorDTO> getAllInstructors();

    // 根据ID获取教师
    InstructorDTO getInstructorById(Integer instructorId);

    // 根据用户ID获取教师
    InstructorDTO getInstructorByUserId(Integer userId);

    // 创建教师
    boolean createInstructor(InstructorRequest request);

    // 更新教师信息
    boolean updateInstructor(Integer instructorId, InstructorRequest request);

    // 删除教师
    boolean deleteInstructor(Integer instructorId);

    // 根据部门获取教师
    List<InstructorDTO> getInstructorsByDepartment(Integer departmentId);

    // 搜索教师
    List<InstructorDTO> searchInstructors(String keyword);
}
