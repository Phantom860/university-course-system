package com.university.university_course_system.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class CourseSection {
    private Integer sectionId;
    private Integer courseId;
    private Integer semesterId;
    private Integer instructorId;
    private String sectionCode;
    private ScheduleType scheduleType;
    private String scheduleInfo;
    private LocalTime startTime;
    private LocalTime endTime;
    private String daysOfWeek; // "MWF" 或 "TT"
    private Integer maxCapacity;
    private Integer currentEnrollment;
    private String classroom;
    private SectionStatus sectionStatus;

    private String semesterCode;

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

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
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

    public SectionStatus getSectionStatus() {
        return sectionStatus;
    }

    public void setSectionStatus(SectionStatus sectionStatus) {
        this.sectionStatus = sectionStatus;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    // 关联对象
    private Course course;
    private Instructor instructor;
    private Semester semester;

    public enum ScheduleType {
        regular, intensive, online
    }

    public enum SectionStatus {
        open, closed, cancelled
    }

    /**
     * 解析daysOfWeek字符串为DayOfWeek集合
     * 支持格式: "MWF" (周一、三、五), "TT" (周二、四), "T" (周二) 等
     */
    public Set<DayOfWeek> getClassDays() {
        Set<DayOfWeek> days = new HashSet<>();

        if (this.daysOfWeek == null || this.daysOfWeek.trim().isEmpty()) {
            return days;
        }

        String normalized = this.daysOfWeek.toUpperCase().trim();

        for (char dayChar : normalized.toCharArray()) {
            DayOfWeek day = charToDayOfWeek(dayChar);
            if (day != null) {
                days.add(day);
            }
        }

        return days;
    }

    /**
     * 将字符转换为DayOfWeek枚举
     */
    private DayOfWeek charToDayOfWeek(char dayChar) {
        switch (dayChar) {
            case 'M': return DayOfWeek.MONDAY;
            case 'T': return DayOfWeek.TUESDAY;
            case 'W': return DayOfWeek.WEDNESDAY;
            case 'R': return DayOfWeek.THURSDAY;  // 有些系统用R表示周四
            case 'F': return DayOfWeek.FRIDAY;
            case 'S': return DayOfWeek.SATURDAY;
            case 'U': return DayOfWeek.SUNDAY;
            default:
                System.out.println("未知的日期字符: " + dayChar);
                return null;
        }
    }

    /**
     * 检查是否在特定日期上课
     */
    public boolean hasClassOn(DayOfWeek day) {
        return getClassDays().contains(day);
    }

    /**
     * 获取学期代码（安全方法）
     */
    public String getSemesterCode() {
        // 优先从 semester 对象获取
        if (this.semester != null && this.semester.getSemesterCode() != null) {
            return this.semester.getSemesterCode();
        }
        // 备选：从 semesterCode 字段获取
        if (this.semesterCode != null && !this.semesterCode.trim().isEmpty()) {
            return this.semesterCode.trim();
        }
        return null;
    }

    /**
     * 检查课程时间是否有效
     */
    public boolean isValidForTimeConflictCheck() {
        return this.startTime != null &&
                this.endTime != null &&
                this.daysOfWeek != null &&
                !this.daysOfWeek.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("CourseSection{id=%d, code=%s, days=%s, time=%s-%s, semesterCode字段=%s, semester对象=%s}",
                sectionId, sectionCode, daysOfWeek, startTime, endTime,
                this.semesterCode,  // 直接显示字段值
                (semester != null ? semester.getSemesterCode() : "null")); // 显示关联对象
    }
}
