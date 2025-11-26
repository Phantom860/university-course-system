package com.university.university_course_system.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CoursePrereq {
    private Integer prereqId;
    private Integer courseId;
    private Integer prereqCourseId;
    private GradeRequirement gradeRequirement;
    private BigDecimal minGrade;
    private Boolean mandatory;

    public Integer getPrereqId() {
        return prereqId;
    }

    public void setPrereqId(Integer prereqId) {
        this.prereqId = prereqId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getPrereqCourseId() {
        return prereqCourseId;
    }

    public void setPrereqCourseId(Integer prereqCourseId) {
        this.prereqCourseId = prereqCourseId;
    }

    public GradeRequirement getGradeRequirement() {
        return gradeRequirement;
    }

    public void setGradeRequirement(GradeRequirement gradeRequirement) {
        this.gradeRequirement = gradeRequirement;
    }

    public BigDecimal getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(BigDecimal minGrade) {
        this.minGrade = minGrade;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getPrereqCourse() {
        return prereqCourse;
    }

    public void setPrereqCourse(Course prereqCourse) {
        this.prereqCourse = prereqCourse;
    }

    // 关联对象
    private Course course;
    private Course prereqCourse;

    public enum GradeRequirement {
        PASS, MINIMUM_GRADE
    }
}
