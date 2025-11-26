// UserMapper.java
package com.university.university_course_system.mapper;

import com.university.university_course_system.entity.Instructor;
import com.university.university_course_system.entity.Student;
import com.university.university_course_system.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    // 根据用户名查找用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    // 根据用户ID查找用户
    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User findById(Integer userId);

    // 学生相关查询
    @Select("SELECT * FROM student WHERE student_number = #{studentNumber}")
    Student findStudentByNumber(String studentNumber);

    @Select("SELECT * FROM student WHERE user_id = #{userId}")
    Student findStudentByUserId(Long userId);

    // 教师相关查询
    @Select("SELECT * FROM instructor WHERE instructor_number = #{instructorNumber}")
    Instructor findInstructorByNumber(String instructorNumber);

    @Select("SELECT * FROM instructor WHERE user_id = #{userId}")
    Instructor findInstructorByUserId(Long userId);

    // 检查用户名是否存在
    @Select("SELECT COUNT(*) > 0 FROM user WHERE username = #{username}")
    boolean existsByUsername(String username);

    // 检查邮箱是否存在
    @Select("SELECT COUNT(*) > 0 FROM user WHERE email = #{email}")
    boolean existsByEmail(String email);

    // 插入用户
    @Insert("INSERT INTO user (username, password_hash, email, phone, user_type, status, created_at) " +
            "VALUES (#{username}, #{passwordHash}, #{email}, #{phone}, #{userType}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(User user);

    // 更新最后登录时间
    @Update("UPDATE user SET last_login = #{lastLogin} WHERE user_id = #{userId}")
    int updateLastLogin(@Param("userId") Integer userId, @Param("lastLogin") LocalDateTime lastLogin);

    // 更新用户状态
    @Update("UPDATE user SET status = #{status} WHERE user_id = #{userId}")
    int updateUserStatus(@Param("userId") Integer userId, @Param("status") String status);

    // 获取待审核用户列表
    @Select("SELECT * FROM user WHERE status = 'pending' ORDER BY created_at DESC")
    List<User> findPendingUsers();

    // 获取所有用户
    @Select("SELECT * FROM user ORDER BY created_at DESC")
    List<User> findAllUsers();

    // 根据状态获取用户列表
    @Select("SELECT * FROM user WHERE status = #{status} ORDER BY created_at DESC")
    List<User> findUsersByStatus(@Param("status") String status);


    @Select("SELECT * FROM user WHERE user_id = #{userId} AND user_type = 'instructor' AND status = 'active'")
    User findActiveInstructorUser(Integer userId);

    @Select("SELECT COUNT(*) > 0 FROM user WHERE user_id = #{userId} AND user_type = 'instructor' AND status = 'active'")
    boolean isActiveInstructorUser(Integer userId);

    @Select("SELECT * FROM user WHERE user_id = #{userId} AND user_type = 'student' AND status = 'active'")
    User findActiveStudentUser(Integer userId);

    @Select("SELECT COUNT(*) > 0 FROM user WHERE user_id = #{userId} AND user_type = 'student' AND status = 'active'")
    boolean isActiveStudentUser(Integer userId);
}