package com.university.university_course_system.mapper;

import com.university.university_course_system.dto.response.CourseSectionDetailDTO;
import com.university.university_course_system.entity.CourseSection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CourseSectionMapper {

    /*
    根据courseid查询sectionid
     */
    @Select("SELECT section_id FROM coursesection WHERE course_id = #{courseId}")
    List<Integer> findSectionIdsByCourseId(@Param("courseId") Integer courseId);

    /*
   根据courseid查询排课信息
    */
    @Select("SELECT * FROM coursesection WHERE course_id = #{courseId}")
    List<CourseSection> findSectionsByCourseId(Integer courseId);

    //根据教师id查询排课信息
    @Select("SELECT * FROM coursesection WHERE instructor_id = #{instructorId}")
    List<CourseSection> findSectionsByInstructorId(@Param("instructorId") Integer instructorId);

    //根据sectionid查询所有课程信息
    @Select("""
SELECT 
    cs.*, 
    c.course_code, c.course_name, c.credits, c.department_id, 
    c.course_type, c.description, c.learning_objectives, c.total_hours,
    d.department_name,
    i.first_name AS instructorFirstName,
    i.last_name AS instructorLastName
FROM coursesection cs
LEFT JOIN course c ON cs.course_id = c.course_id
LEFT JOIN department d ON c.department_id = d.department_id
LEFT JOIN instructor i ON cs.instructor_id = i.instructor_id
WHERE cs.section_id = #{sectionId}
""")
    CourseSectionDetailDTO getSectionDetailById(Integer sectionId);



    @Select("SELECT cs.*, s.semester_code as semesterCode " +  // 使用别名
            "FROM coursesection cs " +
            "LEFT JOIN semester s ON cs.semester_id = s.semester_id " +
            "WHERE cs.section_id = #{sectionId}")
    CourseSection findById(Integer sectionId);

    @Select("SELECT COUNT(*) FROM coursesection WHERE course_id = #{courseId}")
    int countByCourseId(@Param("courseId") Integer courseId);

    // 查询学生在指定学期的已选课程班次
    @Select("SELECT cs.* FROM coursesection cs " +
            "JOIN enrollment e ON cs.section_id = e.section_id " +
            "WHERE e.student_id = #{studentId} " +
            "AND e.enrollment_status IN ('passed', 'enrolling') " +
            "AND cs.semester_id = #{semesterId}")  // 直接使用 semesterId
    List<CourseSection> findEnrolledSectionsByStudentAndSemesterId(
            @Param("studentId") Integer studentId,
            @Param("semesterId") Integer semesterId);

    // 根据教师ID查询所教班次
    @Select("SELECT * FROM coursesection WHERE instructor_id = #{instructorId}")
    List<CourseSection> findByInstructorId(Integer instructorId);

    // 根据学期查询所有班次
    @Select("SELECT * FROM coursesection WHERE semester_id = #{semesterId}")
    List<CourseSection> findBySemesterId(Integer semesterId);

    /**
     * 增加课程段当前选课人数
     */
    @Update("UPDATE coursesection SET current_enrollment = current_enrollment + 1 WHERE section_id = #{sectionId}")
    int incrementCurrentEnrollment(@Param("sectionId") Integer sectionId);

    /**
     * 减少课程段当前选课人数
     */
    @Update("UPDATE coursesection SET current_enrollment = current_enrollment - 1 WHERE section_id = #{sectionId}")
    int decrementCurrentEnrollment(@Param("sectionId") Integer sectionId);

    /**
     * 设置课程段当前选课人数（直接设置）
     */
    @Update("UPDATE coursesection SET current_enrollment = #{count} WHERE section_id = #{sectionId}")
    int setCurrentEnrollment(@Param("sectionId") Integer sectionId, @Param("count") Integer count);

    /**
     * 设置课程段最大容量（管理员用）
     */
    @Update("UPDATE coursesection SET max_capacity = #{maxCapacity} WHERE section_id = #{sectionId}")
    int setMaxCapacity(@Param("sectionId") Integer sectionId, @Param("maxCapacity") Integer maxCapacity);

    /**
     * 更新课程段信息（包括最大容量）
     */
    @Update("UPDATE coursesection SET max_capacity = #{maxCapacity}, " +
            "classroom = #{classroom}, section_status = #{sectionStatus} WHERE section_id = #{sectionId}")
    int updateCourseSection(CourseSection courseSection);
}
