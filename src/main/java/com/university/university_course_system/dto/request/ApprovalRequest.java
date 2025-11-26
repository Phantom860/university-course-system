// ApprovalRequest.java
package com.university.university_course_system.dto.request;

public class ApprovalRequest {
    private Integer userId;
    private String action; // "approve" 或 "reject"
    private String reason; // 拒绝原因（可选）

    // 构造器
    public ApprovalRequest() {}

    // getter 和 setter
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
