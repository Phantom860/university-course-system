// AuthServiceImpl.java
package com.university.university_course_system.service.impl;

import com.university.university_course_system.dto.request.LoginRequest;
import com.university.university_course_system.dto.request.RegisterRequest;

import com.university.university_course_system.dto.response.AuthResponse;
import com.university.university_course_system.dto.response.RegisterResponse;
import com.university.university_course_system.entity.Instructor;
import com.university.university_course_system.entity.Student;
import com.university.university_course_system.entity.User;
import com.university.university_course_system.mapper.InstructorMapper;
import com.university.university_course_system.mapper.StudentMapper;
import com.university.university_course_system.mapper.UserMapper;
import com.university.university_course_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private InstructorMapper instructorMapper;

    /**
     * 根据userId获取学生ID
     */
    public Integer getStudentIdByUserId(Integer userId) {
        return userMapper.findStudentIdByUserId(userId);
    }

    /**
     * 根据userId获取教师ID
     */
    public Integer getInstructorIdByUserId(Integer userId) {
        return userMapper.findInstructorIdByUserId(userId);
    }

    /*
    登录
     */
    @Override
    public AuthResponse login(LoginRequest loginRequest, HttpSession session) {
        AuthResponse response = new AuthResponse();

        try {
            // 1. 根据用户类型查找用户
            User user = null;
            String  userTypeStr = loginRequest.getUserType();

            if ("student".equals(userTypeStr)) {
                // 学生登录 - 通过学号查找学生表，再关联用户表
                Student student = userMapper.findStudentByNumber(loginRequest.getUsername());
                if (student != null) {
                    user = userMapper.findById(student.getUserId());
                }
            } else if ("instructor".equals(userTypeStr)) {
                // 教师登录 - 通过工号查找教师表，再关联用户表
                Instructor instructor = userMapper.findInstructorByNumber(loginRequest.getUsername());
                if (instructor != null) {
                    user = userMapper.findById(instructor.getUserId());
                }
            } else if ("admin".equals(userTypeStr)) {
                user = userMapper.findByUsername(loginRequest.getUsername());
            } else {
                response.setMessage("用户类型不合法");
                return response;
            }

            if (user == null) {
                String userTypeName = "student".equals(userTypeStr) ? "学生" : "教师";
                response.setMessage(userTypeName + "账号不存在");
                return response;
            }

            // 2. 验证用户类型是否匹配
            boolean typeMatch = false;
            if ("student".equalsIgnoreCase(userTypeStr)) {
                typeMatch = (user.getUserType() == User.UserType.student);
            } else if ("instructor".equalsIgnoreCase(userTypeStr) || "teacher".equalsIgnoreCase(userTypeStr)) {
                typeMatch = (user.getUserType() == User.UserType.instructor);
            } else if ("admin".equalsIgnoreCase(userTypeStr)) {
                typeMatch = (user.getUserType() == User.UserType.admin);
            }

            System.out.println("类型匹配结果: " + typeMatch);

            if (!typeMatch) {
                System.out.println("用户类型不匹配: 前端=" + userTypeStr + ", 数据库=" + user.getUserType());
                response.setMessage("用户类型不匹配");
                return response;
            }

            System.out.println("用户类型验证通过");

            // 2. 验证密码（MD5加密）
            String inputPasswordHash = DigestUtils.md5DigestAsHex(loginRequest.getPassword().getBytes());
            if (!inputPasswordHash.equals(user.getPasswordHash())) {
                response.setMessage("密码错误");
                return response;
            }

            // 3. 检查账号状态
            if (user.getStatus() != User.UserStatus.active) {
                response.setMessage("账号未激活，请联系管理员");
                return response;
            }

            // 4. 更新最后登录时间
            user.setLastLogin(LocalDateTime.now());
            userMapper.updateLastLogin(user.getUserId(), user.getLastLogin());

            // 5. 存入Session
            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userType", user.getUserType());
            session.setMaxInactiveInterval(30 * 60); // 30分钟过期

            // 6. 返回成功响应
            response.setToken("session_" + user.getUserId() + "_" + System.currentTimeMillis());
            response.setUser(user);
            response.setMessage("登录成功");

        } catch (Exception e) {
            response.setMessage("登录失败: " + e.getMessage());
        }

        return response;
    }


    /*
    注册
     */
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        RegisterResponse response = new RegisterResponse();

        try {
            // 1. 验证输入
            String validationError = validateRegisterInput(registerRequest);
            if (validationError != null) {
                response.setMessage(validationError);
                response.setSuccess(false);
                return response;
            }

            // 2. 检查用户名和邮箱是否已存在
            if (userMapper.existsByUsername(registerRequest.getUsername())) {
                response.setMessage("用户名已存在");
                response.setSuccess(false);
                return response;
            }
            if (userMapper.existsByEmail(registerRequest.getEmail())) {
                response.setMessage("邮箱已被注册");
                response.setSuccess(false);
                return response;
            }

            // 3. 创建用户对象
            User user = new User();
            user.setUsername(registerRequest.getUsername());

            // 密码加密
            String passwordHash = DigestUtils.md5DigestAsHex(registerRequest.getPassword().getBytes());
            user.setPasswordHash(passwordHash);

            user.setEmail(registerRequest.getEmail());
            user.setPhone(registerRequest.getPhone());

            // 类型转换
            User.UserType userType = convertToUserType(registerRequest.getUserType());
            if (userType == null) {
                response.setMessage("无效的用户类型");
                response.setSuccess(false);
                return response;
            }
            user.setUserType(userType);

            // 设置账号状态为等待审核
            user.setStatus(User.UserStatus.pending);

            // 4. 保存用户到 user 表
            int result = userMapper.insertUser(user);

            if (result <= 0) {
                response.setSuccess(false);
                response.setMessage("注册失败，请重试");
                return response;
            }



            // *************************************************

            response.setSuccess(true);
            response.setMessage("注册成功，等待管理员审核");
            response.setUserId(user.getUserId());

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("注册失败: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }



    /*
    登出
     */
    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }


    /*
    获取当前用户
     */
    @Override
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }


    /*
    验证是否为管理员
     */
    @Override
    public boolean isAdmin(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.getUserType() == User.UserType.admin;
    }

    /*
    验证是否为学生
     */
    @Override
    public boolean isStudent(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.getUserType() == User.UserType.student;
    }

    /*
    验证是否为教师
     */
    @Override
    public boolean isInstructor(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.getUserType() == User.UserType.instructor;
    }

    // 注册信息校验
    private String validateRegisterInput(RegisterRequest request) {
        // 验证用户名
        if (request.getUsername() == null || request.getUsername().length() < 3 || request.getUsername().length() > 50) {
            return "用户名长度必须在3-50字符之间";
        }

        // 验证密码
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return "密码长度至少6位";
        }

        // 验证邮箱
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (request.getEmail() == null || !Pattern.matches(emailRegex, request.getEmail())) {
            return "邮箱格式不正确";
        }

        // 验证手机号
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            String phoneRegex = "^1[3-9]\\d{9}$";
            if (!Pattern.matches(phoneRegex, request.getPhone())) {
                return "手机号格式不正确";
            }
        }

        // 验证用户类型
        if (request.getUserType() == null) {
            return "用户类型不能为空";
        }

        User.UserType userType = convertToUserType(request.getUserType());
        if (userType == null || userType == User.UserType.admin) {
            return "用户类型必须是student或instructor";
        }

        return null;
    }

    private User.UserType convertToUserType(String userTypeStr) {
        if (userTypeStr == null) return null;

        try {
            return User.UserType.valueOf(userTypeStr.toLowerCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    /*
    根据userid修改电话邮箱
     */
    public boolean updateContactInfo(Integer userId, String phone, String email) {
        int updatedRows = userMapper.updatePhoneAndEmailByUserId(userId, phone, email);
        return updatedRows > 0;
    }
}