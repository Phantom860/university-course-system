package com.university.university_course_system.mapper;

import com.university.university_course_system.entity.Student;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface StudentMapper {

    // 根据用户ID查询学生
    @Select("SELECT * FROM student WHERE user_id = #{userId}")
    Student findByUserId(Integer userId);

    // 检查学号是否存在
    @Select("SELECT COUNT(*) > 0 FROM student WHERE student_number = #{studentNumber}")
    boolean existsByStudentNumber(String studentNumber);

    Student findByIdWithDepartment(@Param("studentId") Integer studentId);

    Student findByStudentNumber(@Param("studentNumber") String studentNumber);

    List<Student> findAllWithDepartment();

    @Insert("INSERT INTO student (user_id, student_number, first_name, last_name, gender, " +
            "date_of_birth, department_id, major, admission_year) " +
            "VALUES (#{userId}, #{studentNumber}, #{firstName}, #{lastName}, #{gender}, " +
            "#{dateOfBirth}, #{departmentId}, #{major}, #{admissionYear})")
    @Options(useGeneratedKeys = true, keyProperty = "studentId")
    int insert(Student student);

    // 新的学生注册逻辑，专用于 RegisterRequest
    int insertByRegister(Student student);

    @Update("UPDATE student SET first_name = #{firstName}, last_name = #{lastName}, " +
            "gender = #{gender}, date_of_birth = #{dateOfBirth}, department_id = #{departmentId}, " +
            "major = #{major}, admission_year = #{admissionYear} " +
            "WHERE student_id = #{studentId}")
    int update(Student student);

    @Delete("DELETE FROM student WHERE student_id = #{studentId}")
    int delete(@Param("studentId") Integer studentId);

    @Select("SELECT COUNT(*) FROM student WHERE student_number = #{studentNumber}")
    int countByStudentNumber(@Param("studentNumber") String studentNumber);

    List<Student> findByDepartmentId(@Param("departmentId") Integer departmentId);

    @Select("SELECT ROUND(AVG(s.cumulative_gpa), 2) as avg_gpa " +
            "FROM student s " +
            "WHERE s.department_id = #{departmentId} " +
            "AND s.cumulative_gpa > 0")
    BigDecimal findAverageGpaByDepartment(@Param("departmentId") Integer departmentId);
}