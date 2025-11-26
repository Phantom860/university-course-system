package com.university.university_course_system.mapper;

import com.university.university_course_system.entity.Instructor;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InstructorMapper {

    // 根据ID查询教师
    @Select("SELECT * FROM instructor WHERE instructor_id = #{instructorId}")
    Instructor findById(Integer instructorId);

    // 根据用户ID查询教师
    @Select("SELECT * FROM instructor WHERE user_id = #{userId}")
    Instructor findByUserId(Integer userId);

    // 查询所有教师
    @Select("SELECT * FROM instructor ORDER BY instructor_id")
    List<Instructor> findAll();

    // 根据部门查询教师
    @Select("SELECT * FROM instructor WHERE department_id = #{departmentId} ORDER BY instructor_id")
    List<Instructor> findByDepartmentId(Integer departmentId);

    // 插入教师
    @Insert("INSERT INTO instructor (user_id, employee_number, first_name, last_name, title, department_id, office_location, research_interests) " +
            "VALUES (#{userId}, #{employeeNumber}, #{firstName}, #{lastName}, #{title}, #{departmentId}, #{officeLocation}, #{researchInterests})")
    @Options(useGeneratedKeys = true, keyProperty = "instructorId")
    int insert(Instructor instructor);

    // 更新教师信息
    @Update("UPDATE instructor SET employee_number = #{employeeNumber}, first_name = #{firstName}, last_name = #{lastName}, " +
            "title = #{title}, department_id = #{departmentId}, office_location = #{officeLocation}, research_interests = #{researchInterests} " +
            "WHERE instructor_id = #{instructorId}")
    int update(Instructor instructor);

    // 删除教师
    @Delete("DELETE FROM instructor WHERE instructor_id = #{instructorId}")
    int delete(Integer instructorId);

    // 检查工号是否存在
    @Select("SELECT COUNT(*) > 0 FROM instructor WHERE employee_number = #{employeeNumber}")
    boolean existsByEmployeeNumber(String employeeNumber);

    // 获取教师详细信息（关联用户表）
    @Select("SELECT i.*, u.username, u.email, d.department_name " +
            "FROM instructor i " +
            "LEFT JOIN user u ON i.user_id = u.user_id " +
            "LEFT JOIN department d ON i.department_id = d.department_id " +
            "WHERE i.instructor_id = #{instructorId}")
    Instructor findInstructorDetailById(Integer instructorId);
}