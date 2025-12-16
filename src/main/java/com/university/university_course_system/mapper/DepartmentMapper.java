package com.university.university_course_system.mapper;

import com.university.university_course_system.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department WHERE department_id = #{departmentId}")
    Department findById(@Param("departmentId") Integer departmentId);

    @Insert("INSERT INTO department(department_code, department_name, dean_name, contact_email, contact_phone) " +
            "VALUES(#{departmentCode}, #{departmentName}, #{deanName}, #{contactEmail}, #{contactPhone})")
    @Options(useGeneratedKeys = true, keyProperty = "departmentId")
    int insert(Department department);

    @Delete("DELETE FROM department WHERE department_id = #{departmentId}")
    int deleteById(@Param("departmentId") Integer departmentId);

    @Update("UPDATE department SET department_code=#{departmentCode}, department_name=#{departmentName}, " +
            "dean_name=#{deanName}, contact_email=#{contactEmail}, contact_phone=#{contactPhone} " +
            "WHERE department_id=#{departmentId}")
    int update(Department department);

    @Select("SELECT * FROM department")
    List<Department> selectAll();
}
