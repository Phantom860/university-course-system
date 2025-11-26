// InstructorServiceImpl.java
package com.university.university_course_system.service.impl;

import com.university.university_course_system.dto.response.InstructorDTO;
import com.university.university_course_system.dto.request.InstructorRequest;
import com.university.university_course_system.entity.Instructor;
import com.university.university_course_system.entity.User;
import com.university.university_course_system.mapper.InstructorMapper;
import com.university.university_course_system.mapper.UserMapper;
import com.university.university_course_system.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private InstructorMapper instructorMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<InstructorDTO> getAllInstructors() {
        List<Instructor> instructors = instructorMapper.findAll();
        return instructors.stream()
                .map(InstructorDTO::fromInstructor)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorDTO getInstructorById(Integer instructorId) {
        Instructor instructor = instructorMapper.findInstructorDetailById(instructorId);
        return instructor != null ? InstructorDTO.fromInstructor(instructor) : null;
    }

    @Override
    public InstructorDTO getInstructorByUserId(Integer userId) {
        Instructor instructor = instructorMapper.findByUserId(userId);
        return instructor != null ? InstructorDTO.fromInstructor(instructor) : null;
    }


    @Override
    public boolean createInstructor(InstructorRequest request) {
        try {
            System.out.println("开始创建教师，用户ID: " + request.getUserId());

            // 1. 验证用户是否存在且是活跃的教师
            User user = userMapper.findActiveInstructorUser(request.getUserId());
            if (user == null) {
                System.out.println("用户验证失败: 用户不存在或不是活跃的教师");
                throw new RuntimeException("用户不存在或不是活跃的教师账号");
            }

            System.out.println("用户验证通过: " + user.getUsername() + ", 类型: " + user.getUserType() + ", 状态: " + user.getStatus());

            // 2. 检查该用户是否已经关联了教师信息
            Instructor existingInstructor = instructorMapper.findByUserId(request.getUserId());
            if (existingInstructor != null) {
                System.out.println("该用户已有关联的教师信息，教师ID: " + existingInstructor.getInstructorId());
                throw new RuntimeException("该用户已有关联的教师信息");
            }

            // 3. 检查工号是否已存在
            if (instructorMapper.existsByEmployeeNumber(request.getEmployeeNumber())) {
                System.out.println("工号已存在: " + request.getEmployeeNumber());
                throw new RuntimeException("工号已存在");
            }

            // 4. 创建教师信息
            Instructor instructor = new Instructor();
            instructor.setUserId(request.getUserId());
            instructor.setEmployeeNumber(request.getEmployeeNumber());
            instructor.setFirstName(request.getFirstName());
            instructor.setLastName(request.getLastName());
            instructor.setTitle(request.getTitle());
            instructor.setDepartmentId(request.getDepartmentId());
            instructor.setOfficeLocation(request.getOfficeLocation());
            instructor.setResearchInterests(request.getResearchInterests());

            int result = instructorMapper.insert(instructor);
            boolean success = result > 0;

            if (success) {
                System.out.println("教师创建成功: " + instructor.getInstructorId());
            } else {
                System.out.println("教师创建失败，数据库插入返回: " + result);
            }

            return success;

        } catch (Exception e) {
            System.out.println("创建教师失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateInstructor(Integer instructorId, InstructorRequest request) {
        try {
            Instructor existingInstructor = instructorMapper.findById(instructorId);
            if (existingInstructor == null) {
                throw new RuntimeException("教师不存在");
            }

            // 如果工号有变化，检查新工号是否已存在
            if (!existingInstructor.getEmployeeNumber().equals(request.getEmployeeNumber()) &&
                    instructorMapper.existsByEmployeeNumber(request.getEmployeeNumber())) {
                throw new RuntimeException("工号已存在");
            }

            Instructor instructor = new Instructor();
            instructor.setInstructorId(instructorId);
            instructor.setEmployeeNumber(request.getEmployeeNumber());
            instructor.setFirstName(request.getFirstName());
            instructor.setLastName(request.getLastName());
            instructor.setTitle(request.getTitle());
            instructor.setDepartmentId(request.getDepartmentId());
            instructor.setOfficeLocation(request.getOfficeLocation());
            instructor.setResearchInterests(request.getResearchInterests());

            int result = instructorMapper.update(instructor);
            return result > 0;

        } catch (Exception e) {
            System.out.println("更新教师失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteInstructor(Integer instructorId) {
        try {
            int result = instructorMapper.delete(instructorId);
            return result > 0;
        } catch (Exception e) {
            System.out.println("删除教师失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<InstructorDTO> getInstructorsByDepartment(Integer departmentId) {
        List<Instructor> instructors = instructorMapper.findByDepartmentId(departmentId);
        return instructors.stream()
                .map(InstructorDTO::fromInstructor)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstructorDTO> searchInstructors(String keyword) {
        // 这里可以使用XML配置更复杂的搜索逻辑
        List<Instructor> allInstructors = instructorMapper.findAll();
        return allInstructors.stream()
                .filter(instructor ->
                        instructor.getFirstName().contains(keyword) ||
                                instructor.getLastName().contains(keyword) ||
                                instructor.getEmployeeNumber().contains(keyword) ||
                                instructor.getTitle().contains(keyword))
                .map(InstructorDTO::fromInstructor)
                .collect(Collectors.toList());
    }
}
