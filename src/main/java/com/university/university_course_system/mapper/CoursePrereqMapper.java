package com.university.university_course_system.mapper;

import com.university.university_course_system.entity.CoursePrereq;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface  CoursePrereqMapper {
    @Insert("""
        INSERT INTO courseprereq 
        (course_id, prereq_course_id, grade_requirement, min_grade, mandatory)
        VALUES 
        (#{courseId}, #{prereqCourseId}, #{gradeRequirement}, #{minGrade}, #{mandatory})
    """)
    int insert(CoursePrereq prereq);

    @Delete("DELETE FROM courseprereq WHERE course_id = #{courseId}")
    int deleteByCourseId(Integer courseId);

    @Select("SELECT * FROM courseprereq WHERE course_id = #{courseId}")
    List<CoursePrereq> findByCourseId(Integer courseId);
}
