package com.university.university_course_system.dto.response;

public class RegisterResponse {
    private boolean success;
    private String message;
    private Integer userId;

    // 构造器
    public RegisterResponse() {}

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RegisterResponse(boolean success, String message, Integer userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    // getter 和 setter
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
