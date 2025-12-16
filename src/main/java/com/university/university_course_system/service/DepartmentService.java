package com.university.university_course_system.service;

import com.university.university_course_system.entity.Department;
import com.university.university_course_system.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    public boolean addDepartment(Department department) {
        return departmentMapper.insert(department) > 0;
    }

    public boolean deleteDepartment(Integer departmentId) {
        return departmentMapper.deleteById(departmentId) > 0;
    }

    public boolean updateDepartment(Department department) {
        return departmentMapper.update(department) > 0;
    }

    public Department getDepartmentById(Integer departmentId) {
        return departmentMapper.findById(departmentId);
    }

    public List<Department> getAllDepartments() {
        return departmentMapper.selectAll();
    }
}

