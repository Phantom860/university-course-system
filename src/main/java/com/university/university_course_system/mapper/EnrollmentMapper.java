package com.university.university_course_system.mapper;

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
}