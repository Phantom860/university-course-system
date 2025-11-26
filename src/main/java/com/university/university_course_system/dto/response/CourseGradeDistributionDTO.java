package com.university.university_course_system.dto.response;

import lombok.Data;
import java.util.Map;

@Data
public class CourseGradeDistributionDTO {
    private String courseCode;
    private String courseName;
    private Map<String, Integer> gradeDistribution; // 成绩分布：A:10, B:15, C:8...
    private Map<String, Integer> scoreDistribution; // 分数段分布：90-100:5, 80-89:10...

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

    public Map<String, Integer> getGradeDistribution() {
        return gradeDistribution;
    }

    public void setGradeDistribution(Map<String, Integer> gradeDistribution) {
        this.gradeDistribution = gradeDistribution;
    }

    public Map<String, Integer> getScoreDistribution() {
        return scoreDistribution;
    }

    public void setScoreDistribution(Map<String, Integer> scoreDistribution) {
        this.scoreDistribution = scoreDistribution;
    }
}
