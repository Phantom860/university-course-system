package com.university.university_course_system.service.impl;

import com.university.university_course_system.dto.request.StudentRequest;
import com.university.university_course_system.dto.response.StudentGpaDTO;
import com.university.university_course_system.dto.response.StudentResponse;
import com.university.university_course_system.entity.Student;
import com.university.university_course_system.entity.User;
import com.university.university_course_system.mapper.StudentMapper;
import com.university.university_course_system.mapper.UserMapper;
import com.university.university_course_system.service.ReportService;
import com.university.university_course_system.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.university.university_course_system.entity.User.UserType.student;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReportService reportService;

    @Override
    public StudentResponse getStudentById(Integer studentId) {
        Student student = studentMapper.findByIdWithDepartment(studentId);
        StudentResponse response = convertToResponse(student);

        // 🔥 关键：补充 GPA 信息
        StudentGpaDTO gpaDTO = reportService.getStudentGpa(studentId);
        if (gpaDTO != null) {
            response.setTotalCredits(gpaDTO.getTotalCredits());
            response.setCumulativeGpa(gpaDTO.getCumulativeGpa());
        }

        return response;
    }

    @Override
    public StudentResponse getStudentByNumber(String studentNumber) {
        Student student = studentMapper.findByStudentNumber(studentNumber);
        return convertToResponse(student);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentMapper.findAllWithDepartment();
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public StudentResponse createStudent(StudentRequest studentRequest, HttpSession session) {
        try {
            System.out.println("开始创建学生，用户ID: " + studentRequest.getUserId());

            // 1. 验证用户是否存在且是活跃的学生
            User user = userMapper.findActiveStudentUser(studentRequest.getUserId());
            if (user == null) {
                System.out.println("用户验证失败: 用户不存在或不是活跃的学生账号");
                throw new RuntimeException("用户不存在或不是活跃的学生账号");
            }

            System.out.println("用户验证通过: " + user.getUsername() + ", 类型: " + user.getUserType() + ", 状态: " + user.getStatus());

            // 2. 检查该用户是否已经关联了学生信息
            Student existingStudent = studentMapper.findByUserId(studentRequest.getUserId());
            if (existingStudent != null) {
                System.out.println("该用户已有关联的学生信息，学生ID: " + existingStudent.getStudentId());
                throw new RuntimeException("该用户已有关联的学生信息");
            }

            // 3. 检查学号是否已存在
            if (studentMapper.existsByStudentNumber(studentRequest.getStudentNumber())) {
                System.out.println("学号已存在: " + studentRequest.getStudentNumber());
                throw new RuntimeException("学号已存在: " + studentRequest.getStudentNumber());
            }

            // 4. 创建学生信息
            Student student = new Student();
            BeanUtils.copyProperties(studentRequest, student);
            // 设置默认值
            student.setTotalCredits(BigDecimal.valueOf(0.0));
            student.setCumulativeGpa(BigDecimal.valueOf(0.0));

            int result = studentMapper.insert(student);

            if (result > 0) {
                System.out.println("学生创建成功: " + student.getStudentId());
                return getStudentById(student.getStudentId());
            } else {
                System.out.println("学生创建失败，数据库插入返回: " + result);
                throw new RuntimeException("学生创建失败");
            }

        } catch (RuntimeException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            System.out.println("创建学生失败: " + e.getMessage());
            throw new RuntimeException("创建学生失败: " + e.getMessage());
        }
    }

    @Override
    public StudentResponse updateStudent(Integer studentId, StudentRequest studentRequest, HttpSession session) {
        Student existingStudent = studentMapper.findByIdWithDepartment(studentId);
        if (existingStudent == null) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        // 检查学号是否被其他学生使用
        Student studentWithSameNumber = studentMapper.findByStudentNumber(studentRequest.getStudentNumber());
        if (studentWithSameNumber != null && !studentWithSameNumber.getStudentId().equals(studentId)) {
            throw new RuntimeException("学号已被其他学生使用: " + studentRequest.getStudentNumber());
        }

        Student student = new Student();
        BeanUtils.copyProperties(studentRequest, student);
        student.setStudentId(studentId);

        studentMapper.update(student);
        return getStudentById(studentId);
    }

    @Override
    public void deleteStudent(Integer studentId, HttpSession session) {
        // 查询学生及对应的用户账号
        Student student = studentMapper.findByIdWithDepartment(studentId);
        if (student == null) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        // 将对应的 user 账号状态设置为 inactive（软删除）
        int updated = userMapper.deactivateUser(student.getUserId());
        if (updated == 0) {
            throw new RuntimeException("无法注销该学生的用户账号，ID: " + student.getUserId());
        }

        studentMapper.delete(studentId);
    }

    @Override
    public boolean existsByStudentNumber(String studentNumber) {
        return studentMapper.countByStudentNumber(studentNumber) > 0;
    }

    private StudentResponse convertToResponse(Student student) {
        if (student == null) {
            return null;
        }

        StudentResponse response = new StudentResponse();
        BeanUtils.copyProperties(student, response);

        // 设置关联的院系名称
        if (student.getDepartment() != null) {
            response.setDepartmentName(student.getDepartment().getDepartmentName());
        }

        return response;
    }
}
