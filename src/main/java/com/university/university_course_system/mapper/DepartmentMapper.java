package com.university.university_course_system.mapper;

import com.university.university_course_system.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department WHERE department_id = #{departmentId}")
    Department findById(@Param("departmentId") Integer departmentId);
}
