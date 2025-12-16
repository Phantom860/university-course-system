package com.university.university_course_system.mapper;

import com.university.university_course_system.dto.response.GradeStatisticsDTO;
import com.university.university_course_system.entity.Course;
import com.university.university_course_system.entity.CourseSection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    Course findByIdWithDepartment(@Param("courseId") Integer courseId);

    Course findByCourseCode(@Param("courseCode") String courseCode);

    List<Course> findAllWithDepartment();

    List<Course> findByDepartmentId(@Param("departmentId") Integer departmentId);

    @Insert("INSERT INTO course (course_code, course_name, credits, department_id, course_type, description, total_hours) " +
            "VALUES (#{courseCode}, #{courseName}, #{credits}, #{departmentId}, #{courseType}, #{description}, #{totalHours})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    int insert(Course course);

    @Update("UPDATE course SET course_code = #{courseCode}, course_name = #{courseName}, " +
            "credits = #{credits}, department_id = #{departmentId}, course_type = #{courseType}, " +
            "description = #{description}, total_hours = #{totalHours} " +
            "WHERE course_id = #{courseId}")
    int update(Course course);

    @Delete("DELETE FROM course WHERE course_id = #{courseId}")
    int delete(@Param("courseId") Integer courseId);

    @Select("SELECT COUNT(*) FROM course WHERE course_code = #{courseCode}")
    int countByCourseCode(@Param("courseCode") String courseCode);

    //根据课程名查 courseId
    @Select("SELECT * FROM course WHERE course_name = #{courseName}")
    Course findByCourseName(String courseName);

    List<GradeStatisticsDTO> findCourseStatisticsByDepartment(@Param("departmentId") Integer departmentId);
}