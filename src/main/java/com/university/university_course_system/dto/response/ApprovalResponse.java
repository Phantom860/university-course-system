// ApprovalResponse.java
package com.university.university_course_system.dto.response;

public class ApprovalResponse {
    private boolean success;
    private String message;

    // 构造器
    public ApprovalResponse() {}

    public ApprovalResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // getter 和 setter
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
