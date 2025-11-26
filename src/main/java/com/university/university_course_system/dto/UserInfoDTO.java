// UserInfoDTO.java
package com.university.university_course_system.dto;

import com.university.university_course_system.entity.User;

import java.time.LocalDateTime;

public class UserInfoDTO {
    private Integer userId;
    private String username;
    private String email;
    private String phone;
    private String userType;
    private String status;
    private LocalDateTime createdAt;
    private String name; // 需要从学生表或教师表关联获取

    // 从User实体转换
    public static UserInfoDTO fromUser(User user) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUserType(user.getUserType().name());
        dto.setStatus(user.getStatus().name());
        dto.setCreatedAt(user.getCreatedAt());
        // name 需要额外查询设置
        return dto;
    }

    // getter 和 setter
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
