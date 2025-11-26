package com.university.university_course_system.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Semester {
    private Integer semesterId;
    private String semesterCode;
    private String semesterName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
    private LocalDate addDropDeadline;
    private SemesterStatus semesterStatus;

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(LocalDate registrationStart) {
        this.registrationStart = registrationStart;
    }

    public LocalDate getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(LocalDate registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public LocalDate getAddDropDeadline() {
        return addDropDeadline;
    }

    public void setAddDropDeadline(LocalDate addDropDeadline) {
        this.addDropDeadline = addDropDeadline;
    }

    public SemesterStatus getSemesterStatus() {
        return semesterStatus;
    }

    public void setSemesterStatus(SemesterStatus semesterStatus) {
        this.semesterStatus = semesterStatus;
    }

    public enum SemesterStatus {
        UPCOMING, CURRENT, COMPLETED
    }
}
