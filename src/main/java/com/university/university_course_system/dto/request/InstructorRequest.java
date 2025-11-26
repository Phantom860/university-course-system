// InstructorRequest.java
package com.university.university_course_system.dto.request;

public class InstructorRequest {
    private Integer userId;
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String title;
    private Integer departmentId;
    private String officeLocation;
    private String researchInterests;

    // Getter和Setter
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
}
