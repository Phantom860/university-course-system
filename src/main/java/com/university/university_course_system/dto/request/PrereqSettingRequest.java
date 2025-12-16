package com.university.university_course_system.dto.request;

import com.university.university_course_system.entity.CoursePrereq;
import lombok.Data;

import java.util.List;

@Data
public class PrereqSettingRequest {
    private Integer courseId;
    private List<CoursePrereq> prereqs;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public List<CoursePrereq> getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(List<CoursePrereq> prereqs) {
        this.prereqs = prereqs;
    }
}
