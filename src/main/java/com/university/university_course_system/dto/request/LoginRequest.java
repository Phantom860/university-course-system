package com.university.university_course_system.dto.request;

import lombok.Data;


@Data
public class LoginRequest {
    // 手动添加getter/setter
    private String username;
    private String password;

    public String  getUserType() {
        return userType;
    }

    public void setUserType(String  userType) {
        this.userType = userType;
    }

    private String  userType; // 1-学生，2-教师

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
