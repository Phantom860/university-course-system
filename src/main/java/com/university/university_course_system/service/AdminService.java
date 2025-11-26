// AdminService.java
package com.university.university_course_system.service;

import com.university.university_course_system.dto.UserInfoDTO;
import com.university.university_course_system.dto.request.ApprovalRequest;
import com.university.university_course_system.dto.response.ApprovalResponse;
import com.university.university_course_system.entity.CourseSection;

import java.util.List;
import java.util.Map;

public interface AdminService {

    // 获取待审核用户列表
    List<UserInfoDTO> getPendingUsers();

    // 根据状态获取用户列表
    List<UserInfoDTO> getUsersByStatus(String status);

    // 获取所有用户
    List<UserInfoDTO> getAllUsers();

    // 审核用户
    ApprovalResponse approveUser(ApprovalRequest request);

    // 批量审核用户
    ApprovalResponse batchApproveUsers(List<Integer> userIds);

    // 获取用户统计信息
    Map<String, Object> getUserStats();

    // 根据ID获取用户
    com.university.university_course_system.entity.User getUserById(Integer userId);

    /**
     * 设置课程段最大容量
     */
    boolean setMaxCapacity(Integer sectionId, Integer maxCapacity);

    /**
     * 更新课程段信息
     */
    boolean updateCourseSection(CourseSection courseSection);

    /**
     * 检查容量设置是否合理
     */
    boolean validateCapacity(Integer sectionId, Integer newMaxCapacity);
}