// AdminServiceImpl.java
package com.university.university_course_system.service.impl;

import com.university.university_course_system.dto.UserInfoDTO;
import com.university.university_course_system.dto.request.ApprovalRequest;
import com.university.university_course_system.dto.response.ApprovalResponse;
import com.university.university_course_system.entity.CourseSection;
import com.university.university_course_system.entity.User;
import com.university.university_course_system.mapper.CourseSectionMapper;
import com.university.university_course_system.mapper.UserMapper;
import com.university.university_course_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    /*
    获取待审核用户
     */
    @Override
    public List<UserInfoDTO> getPendingUsers() {
        List<User> pendingUsers = userMapper.findPendingUsers();
        return pendingUsers.stream()
                .map(UserInfoDTO::fromUser)
                .collect(Collectors.toList());
    }


    /*
    获取用户状态
     */
    @Override
    public List<UserInfoDTO> getUsersByStatus(String status) {
        List<User> users = userMapper.findUsersByStatus(status);
        return users.stream()
                .map(UserInfoDTO::fromUser)
                .collect(Collectors.toList());
    }


    /*
    获取所有用户
     */
    @Override
    public List<UserInfoDTO> getAllUsers() {
        List<User> allUsers = userMapper.findAllUsers();
        return allUsers.stream()
                .map(UserInfoDTO::fromUser)
                .collect(Collectors.toList());
    }


    /*
    审核
     */
    @Override
    public ApprovalResponse approveUser(ApprovalRequest request) {
        try {
            System.out.println("审核用户: " + request.getUserId() + ", 操作: " + request.getAction());

            User user = userMapper.findById(request.getUserId());
            if (user == null) {
                return new ApprovalResponse(false, "用户不存在");
            }

            if (user.getStatus() != User.UserStatus.pending) {
                return new ApprovalResponse(false, "用户状态不是待审核");
            }

            if ("approve".equalsIgnoreCase(request.getAction())) {
                // 批准用户
                int result = userMapper.updateUserStatus(request.getUserId(), "active");
                if (result > 0) {
                    System.out.println("用户 " + request.getUserId() + " 审核通过");
                    return new ApprovalResponse(true, "用户审核通过");
                } else {
                    return new ApprovalResponse(false, "审核操作失败");
                }
            } else if ("reject".equalsIgnoreCase(request.getAction())) {
                // 拒绝用户
                int result = userMapper.updateUserStatus(request.getUserId(), "inactive");
                String message = "用户注册已拒绝";
                if (request.getReason() != null && !request.getReason().trim().isEmpty()) {
                    message += "，原因：" + request.getReason();
                }
                if (result > 0) {
                    System.out.println("用户 " + request.getUserId() + " 审核拒绝");
                    return new ApprovalResponse(true, message);
                } else {
                    return new ApprovalResponse(false, "拒绝操作失败");
                }
            } else {
                return new ApprovalResponse(false, "无效的操作类型");
            }

        } catch (Exception e) {
            System.out.println("审核失败: " + e.getMessage());
            return new ApprovalResponse(false, "审核失败: " + e.getMessage());
        }
    }


    /*
    批量审核
     */
    @Override
    public ApprovalResponse batchApproveUsers(List<Integer> userIds) {
        try {
            int successCount = 0;
            for (Integer userId : userIds) {
                User user = userMapper.findById(userId);
                if (user != null && user.getStatus() == User.UserStatus.pending) {
                    int result = userMapper.updateUserStatus(userId, "active");
                    if (result > 0) {
                        successCount++;
                        System.out.println("批量审核通过用户: " + userId);
                    }
                }
            }
            return new ApprovalResponse(true, "批量审核完成，成功处理 " + successCount + " 个用户");
        } catch (Exception e) {
            return new ApprovalResponse(false, "批量审核失败: " + e.getMessage());
        }
    }


    /*
    批量获取用户状态
     */
    @Override
    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        List<User> allUsers = userMapper.findAllUsers();
        List<User> pendingUsers = userMapper.findPendingUsers();
        List<User> activeUsers = userMapper.findUsersByStatus("active");
        List<User> inactiveUsers = userMapper.findUsersByStatus("inactive");

        stats.put("totalUsers", allUsers.size());
        stats.put("pendingUsers", pendingUsers.size());
        stats.put("activeUsers", activeUsers.size());
        stats.put("inactiveUsers", inactiveUsers.size());

        return stats;
    }


    /*
    根据id获取用户
     */
    @Override
    public User getUserById(Integer userId) {
        return userMapper.findById(userId);
    }


    /*
    设置课程最大容量
     */
    @Override
    public boolean setMaxCapacity(Integer sectionId, Integer maxCapacity) {
        try {
            System.out.println("🛠️ 管理员设置课程容量...");
            System.out.println("课程段ID: " + sectionId + ", 新最大容量: " + maxCapacity);

            // 1. 验证参数
            if (sectionId == null || maxCapacity == null) {
                throw new RuntimeException("参数不能为空");
            }

            if (maxCapacity <= 0) {
                throw new RuntimeException("最大容量必须大于0");
            }

            // 2. 获取当前课程信息
            CourseSection currentSection = courseSectionMapper.findById(sectionId);
            if (currentSection == null) {
                throw new RuntimeException("课程段不存在");
            }

            // 3. 检查新容量是否合理
            if (!validateCapacity(sectionId, maxCapacity)) {
                throw new RuntimeException("新容量设置不合理");
            }

            // 4. 执行更新
            int updated = courseSectionMapper.setMaxCapacity(sectionId, maxCapacity);
            if (updated == 0) {
                throw new RuntimeException("更新失败");
            }

            // 5. 获取更新后的信息
            CourseSection updatedSection = courseSectionMapper.findById(sectionId);
            System.out.println("✅ 容量设置成功!");
            System.out.println("原容量: " + currentSection.getMaxCapacity());
            System.out.println("新容量: " + updatedSection.getMaxCapacity());
            System.out.println("当前选课人数: " + updatedSection.getCurrentEnrollment());

            return true;

        } catch (Exception e) {
            System.out.println("❌ 设置容量失败: " + e.getMessage());
            throw new RuntimeException("设置容量失败: " + e.getMessage());
        }
    }

    @Override
    public boolean validateCapacity(Integer sectionId, Integer newMaxCapacity) {
        CourseSection section = courseSectionMapper.findById(sectionId);
        if (section == null) {
            throw new RuntimeException("课程段不存在");
        }

        // 检查新容量是否小于当前选课人数
        if (newMaxCapacity < section.getCurrentEnrollment()) {
            throw new RuntimeException("新容量(" + newMaxCapacity + ")不能小于当前选课人数(" + section.getCurrentEnrollment() + ")");
        }

        // 检查容量是否在合理范围内
        if (newMaxCapacity > 500) {
            throw new RuntimeException("容量不能超过500人");
        }

        if (newMaxCapacity < 1) {
            throw new RuntimeException("容量必须至少为1人");
        }

        return true;
    }

    @Override
    public boolean updateCourseSection(CourseSection courseSection) {
        try {
            System.out.println("🛠️ 管理员更新课程段信息...");
            System.out.println("课程段ID: " + courseSection.getSectionId());

            // 验证参数
            if (courseSection.getSectionId() == null) {
                throw new RuntimeException("课程段ID不能为空");
            }

            // 如果更新了最大容量，需要验证
            if (courseSection.getMaxCapacity() != null) {
                validateCapacity(courseSection.getSectionId(), courseSection.getMaxCapacity());
            }

            // 执行更新
            int updated = courseSectionMapper.updateCourseSection(courseSection);
            if (updated == 0) {
                throw new RuntimeException("更新失败");
            }

            System.out.println("✅ 课程段信息更新成功!");
            return true;

        } catch (Exception e) {
            System.out.println("❌ 更新课程段信息失败: " + e.getMessage());
            throw new RuntimeException("更新课程段信息失败: " + e.getMessage());
        }
    }
}
