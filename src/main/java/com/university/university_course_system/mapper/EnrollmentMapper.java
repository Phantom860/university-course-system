package com.university.university_course_system.mapper;

import com.university.university_course_system.dto.response.CompletedCourseInfo;
import com.university.university_course_system.dto.response.EnrollmentDetailDTO;
import com.university.university_course_system.entity.Enrollment;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface EnrollmentMapper {

    Enrollment findByIdWithDetails(@Param("enrollmentId") Integer enrollmentId);

    List<Enrollment> findByStudentId(@Param("studentId") Integer studentId);

    List<Enrollment> findBySectionId(@Param("sectionId") Integer sectionId);

    @Insert("INSERT INTO enrollment (student_id, section_id, semester_id, enrollment_status) " +
            "VALUES (#{studentId}, #{sectionId}, #{semesterId}, 'enrolling')")
    @Options(useGeneratedKeys = true, keyProperty = "enrollmentId")
    int insert(Enrollment enrollment);

    @Update("UPDATE enrollment SET enrollment_status = #{enrollmentStatus} " +
            "WHERE enrollment_id = #{enrollmentId}")
    int updateStatus(@Param("enrollmentId") Integer enrollmentId, @Param("enrollmentStatus") String enrollmentStatus);

    @Update("UPDATE enrollment SET numeric_grade = #{numericGrade}, letter_grade = #{letterGrade}, " +
            "grade_points = #{gradePoints}, enrollment_status = #{enrollmentStatus} " +
            "WHERE enrollment_id = #{enrollmentId}")
    int updateGrade(Enrollment enrollment);

    @Delete("DELETE FROM enrollment WHERE enrollment_id = #{enrollmentId}")
    int delete(@Param("enrollmentId") Integer enrollmentId);

    @Select("SELECT COUNT(*) FROM enrollment WHERE student_id = #{studentId} AND section_id = #{sectionId}")
    int existsByStudentAndSection(@Param("studentId") Integer studentId, @Param("sectionId") Integer sectionId);

    @Select("SELECT cs.semester_id FROM coursesection cs WHERE cs.section_id = #{sectionId}")
    Integer findSemesterIdBySection(@Param("sectionId") Integer sectionId);

    Map<String, Object> getGradeDistributionByCourse(@Param("courseId") Integer courseId);

    Map<String, Object> getScoreDistributionByCourse(@Param("courseId") Integer courseId);

    //根据sectionid获取所有选了该排课的选课信息（enrollmentid,学生姓名学号，老师姓名等）
    List<EnrollmentDetailDTO> findEnrollmentsBySectionId(@Param("sectionId") Integer sectionId);


    //获取已通过课程
    @Select("""
    SELECT 
        cs.course_id AS courseId,
        e.numeric_grade AS numericGrade
    FROM enrollment e
    JOIN coursesection cs ON e.section_id = cs.section_id
    WHERE 
        e.student_id = #{studentId}
        AND e.enrollment_status = 'passed'
""")
    List<CompletedCourseInfo> findCompletedCourses(Integer studentId);

}