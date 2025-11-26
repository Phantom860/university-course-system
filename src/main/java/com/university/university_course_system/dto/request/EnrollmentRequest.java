package com.university.university_course_system.dto.request;

import lombok.Data;

@Data
public class EnrollmentRequest {
    private Integer studentId;
    private Integer sectionId;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }
}
