// RegisterRequest.java
package com.university.university_course_system.dto.request;

import com.university.university_course_system.entity.User;

public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // 构造器
    public RegisterRequest() {}

    // getter 和 setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }


}