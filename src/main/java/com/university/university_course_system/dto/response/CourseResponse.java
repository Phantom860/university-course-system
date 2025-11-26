package com.university.university_course_system.dto.response;

import com.university.university_course_system.entity.Course;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CourseResponse {
    private Integer courseId;
    private String courseCode;
    private String courseName;
    private BigDecimal credits;
    private String departmentName;
    private Course.CourseType courseType;
    private String description;

    public Course.CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(Course.CourseType courseType) {
        this.courseType = courseType;
    }

    private Integer totalHours;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

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

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }
}
