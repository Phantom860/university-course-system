package com.university.university_course_system.dto.response;

import com.university.university_course_system.entity.Course;
import com.university.university_course_system.entity.CourseSection;
import com.university.university_course_system.entity.Department;
import com.university.university_course_system.entity.Instructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class CourseSectionDetailDTO {

    private Integer sectionId;

    private Integer courseId;
    private String courseCode;
    private String courseName;
    private BigDecimal credits;
    private Integer departmentId;
    private String departmentName;
    private Course.CourseType courseType;
    private String description;
    private String learningObjectives;
    private Integer totalHours;

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Course.CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(Course.CourseType courseType) {
        this.courseType = courseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorFirstName() {
        return instructorFirstName;
    }

    public void setInstructorFirstName(String instructorFirstName) {
        this.instructorFirstName = instructorFirstName;
    }

    public String getInstructorLastName() {
        return instructorLastName;
    }

    public void setInstructorLastName(String instructorLastName) {
        this.instructorLastName = instructorLastName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public CourseSection.ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(CourseSection.ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(String scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getCurrentEnrollment() {
        return currentEnrollment;
    }

    public void setCurrentEnrollment(Integer currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public CourseSection.SectionStatus getSectionStatus() {
        return sectionStatus;
    }

    public void setSectionStatus(CourseSection.SectionStatus sectionStatus) {
        this.sectionStatus = sectionStatus;
    }

    private Integer semesterId;
    private Integer instructorId;
    private String instructorFirstName;
    private String instructorLastName;

    private String sectionCode;
    private CourseSection.ScheduleType scheduleType;
    private String scheduleInfo;
    private LocalTime startTime;
    private LocalTime endTime;
    private String daysOfWeek; // "MWF" 或 "TT"
    private Integer maxCapacity;
    private Integer currentEnrollment;
    private String classroom;
    private CourseSection.SectionStatus sectionStatus;
}
