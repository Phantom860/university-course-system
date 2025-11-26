package com.university.university_course_system.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GradeStatisticsDTO {
    private String courseCode;
    private String courseName;
    private Integer totalStudents;
    private Integer passedStudents;
    private Integer failedStudents;
    private BigDecimal passRate;
    private BigDecimal averageGrade;
    private BigDecimal maxGrade;
    private BigDecimal minGrade;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Integer getPassedStudents() {
        return passedStudents;
    }

    public void setPassedStudents(Integer passedStudents) {
        this.passedStudents = passedStudents;
    }

    public Integer getFailedStudents() {
        return failedStudents;
    }

    public void setFailedStudents(Integer failedStudents) {
        this.failedStudents = failedStudents;
    }

    public BigDecimal getPassRate() {
        return passRate;
    }

    public void setPassRate(BigDecimal passRate) {
        this.passRate = passRate;
    }

    public BigDecimal getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(BigDecimal averageGrade) {
        this.averageGrade = averageGrade;
    }

    public BigDecimal getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(BigDecimal maxGrade) {
        this.maxGrade = maxGrade;
    }

    public BigDecimal getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(BigDecimal minGrade) {
        this.minGrade = minGrade;
    }
}
