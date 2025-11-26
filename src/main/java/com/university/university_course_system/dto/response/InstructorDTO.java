package com.university.university_course_system.dto.response;

import com.university.university_course_system.entity.Instructor;

public class InstructorDTO {
    private Integer instructorId;
    private Integer userId;
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String title;
    private Integer departmentId;
    private String officeLocation;
    private String researchInterests;
    private String username;
    private String email;
    private String departmentName; // 部门名称

    // 从Instructor实体转换
    public static InstructorDTO fromInstructor(Instructor instructor) {
        InstructorDTO dto = new InstructorDTO();
        dto.setInstructorId(instructor.getInstructorId());
        dto.setUserId(instructor.getUserId());
        dto.setEmployeeNumber(instructor.getEmployeeNumber());
        dto.setFirstName(instructor.getFirstName());
        dto.setLastName(instructor.getLastName());
        dto.setTitle(instructor.getTitle());
        dto.setDepartmentId(instructor.getDepartmentId());
        dto.setOfficeLocation(instructor.getOfficeLocation());
        dto.setResearchInterests(instructor.getResearchInterests());
        return dto;
    }

    // Getter和Setter
    public Integer getInstructorId() { return instructorId; }
    public void setInstructorId(Integer instructorId) { this.instructorId = instructorId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }

    public String getResearchInterests() { return researchInterests; }
    public void setResearchInterests(String researchInterests) { this.researchInterests = researchInterests; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
