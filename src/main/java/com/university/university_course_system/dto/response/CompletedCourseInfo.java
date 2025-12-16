package com.university.university_course_system.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompletedCourseInfo {
    private Integer courseId;
    private BigDecimal numericGrade;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public BigDecimal getNumericGrade() {
        return numericGrade;
    }

    public void setNumericGrade(BigDecimal numericGrade) {
        this.numericGrade = numericGrade;
    }
}
