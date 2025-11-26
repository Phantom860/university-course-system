package com.university.university_course_system.dto.response;

import lombok.Data;
import com.university.university_course_system.entity.User;

@Data
public class AuthResponse {
    private String token;
    private User user;
    private String message;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
