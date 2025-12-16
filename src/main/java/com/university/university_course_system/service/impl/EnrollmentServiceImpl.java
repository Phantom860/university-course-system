package com.university.university_course_system.service.impl;

import com.university.university_course_system.dto.request.EnrollmentRequest;
import com.university.university_course_system.dto.request.GradeUpdateRequest;
import com.university.university_course_system.dto.response.CompletedCourseInfo;
import com.university.university_course_system.dto.response.EnrollmentDetailDTO;
import com.university.university_course_system.dto.response.EnrollmentResponse;
import com.university.university_course_system.entity.CoursePrereq;
import com.university.university_course_system.entity.CourseSection;
import com.university.university_course_system.entity.Enrollment;
import com.university.university_course_system.entity.Student;
import com.university.university_course_system.mapper.CoursePrereqMapper;
import com.university.university_course_system.mapper.CourseSectionMapper;
import com.university.university_course_system.mapper.EnrollmentMapper;
import com.university.university_course_system.mapper.StudentMapper;
import com.university.university_course_system.service.CourseScheduleService;
import com.university.university_course_system.service.EnrollmentService;
import com.university.university_course_system.util.GradeCalculator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseScheduleService courseScheduleService;

    @Autowired
    private CoursePrereqMapper coursePrereqMapper;

    @Override
    public EnrollmentResponse getEnrollmentById(Integer enrollmentId) {
        Enrollment enrollment = enrollmentMapper.findByIdWithDetails(enrollmentId);
        return convertToResponse(enrollment);
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByStudent(Integer studentId) {
        List<Enrollment> enrollments = enrollmentMapper.findByStudentId(studentId);
        return enrollments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }




    /*
    根据sectionid获取所有选了该排课的选课信息（enrollmentid,学生姓名学号，老师姓名等）
     */
    public List<EnrollmentDetailDTO> getEnrollmentsBySectionId(Integer sectionId) {
        return enrollmentMapper.findEnrollmentsBySectionId(sectionId);
    }

    /*
    学生选课
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EnrollmentResponse enrollCourse(EnrollmentRequest enrollmentRequest, HttpSession session) {
        System.out.println("🎯 开始选课流程...");

        // 1. 获取课程信息
        CourseSection newSection = courseSectionMapper.findById(enrollmentRequest.getSectionId());
        if (newSection == null) {
            throw new RuntimeException("课程班次不存在");
        }

        System.out.println("课程信息: " + newSection.getSectionCode() +
                " (" + newSection.getDaysOfWeek() + " " +
                newSection.getStartTime() + "-" + newSection.getEndTime() + ")");

        // 2. 检查是否已选过该课程
        if (enrollmentMapper.existsByStudentAndSection(
                enrollmentRequest.getStudentId(), enrollmentRequest.getSectionId()) > 0) {
            throw new RuntimeException("已经选过该课程");
        }

        // 3. 检查课程容量
        if (newSection.getCurrentEnrollment() >= newSection.getMaxCapacity()) {
            throw new RuntimeException("课程已满员");
        }

        // 4. 检查时间冲突
        System.out.println("⏰ 检查时间冲突...");
        if (courseScheduleService.hasTimeConflictWithEnrolledCourses(
                enrollmentRequest.getStudentId(), newSection)) {
            throw new RuntimeException("时间冲突，无法选课");
        }

        // 5. 检查先修课程
        if (!checkPrerequisites(enrollmentRequest.getStudentId(), enrollmentRequest.getSectionId())) {
            throw new RuntimeException("未满足先修课程要求");
        }

        // 6. 执行选课
        System.out.println("✅ 所有检查通过，执行选课...");
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(enrollmentRequest.getStudentId());
        enrollment.setSectionId(enrollmentRequest.getSectionId());

        // 设置学期ID
        Integer semesterId = enrollmentMapper.findSemesterIdBySection(enrollmentRequest.getSectionId());
        enrollment.setSemesterId(semesterId);

        enrollmentMapper.insert(enrollment);

        // 更新课程段当前选课人数
        courseSectionMapper.incrementCurrentEnrollment(enrollmentRequest.getSectionId());

        System.out.println("🎉 选课成功！");
        return getEnrollmentById(enrollment.getEnrollmentId());
    }

    @Override
    public EnrollmentResponse dropCourse(Integer enrollmentId, HttpSession session) {
        Enrollment enrollment = enrollmentMapper.findByIdWithDetails(enrollmentId);
        if (enrollment == null) {
            throw new RuntimeException("选课记录不存在");
        }

        enrollmentMapper.delete(enrollmentId);

        // 更新课程段当前选课人数
        courseSectionMapper.decrementCurrentEnrollment(enrollment.getSectionId());

        return getEnrollmentById(enrollmentId);
    }

    @Override
    public EnrollmentResponse updateGrade(Integer enrollmentId, GradeUpdateRequest gradeRequest, HttpSession session) {
        Enrollment enrollment = enrollmentMapper.findByIdWithDetails(enrollmentId);
        if (enrollment == null) {
            throw new RuntimeException("选课记录不存在");
        }

        // 计算绩点
        BigDecimal gradePoints = GradeCalculator.calculateGradePoints(gradeRequest.getNumericGrade());

        // 确定最终状态
        String finalStatus = gradeRequest.getNumericGrade().compareTo(new BigDecimal("60")) >= 0 ? "passed" : "failed";

        Enrollment updateEnrollment = new Enrollment();
        updateEnrollment.setEnrollmentId(enrollmentId);
        updateEnrollment.setNumericGrade(gradeRequest.getNumericGrade());
        updateEnrollment.setLetterGrade(gradeRequest.getLetterGrade());
        updateEnrollment.setGradePoints(gradePoints);
        updateEnrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.valueOf(finalStatus));

        enrollmentMapper.updateGrade(updateEnrollment);
        return getEnrollmentById(enrollmentId);
    }

    @Override
    public void deleteEnrollment(Integer enrollmentId) {
        Enrollment enrollment = enrollmentMapper.findByIdWithDetails(enrollmentId);
        if (enrollment == null) {
            throw new RuntimeException("选课记录不存在");
        }
        enrollmentMapper.delete(enrollmentId);

        // 更新课程段当前选课人数
        courseSectionMapper.decrementCurrentEnrollment(enrollment.getSectionId());
    }


    /*
    检查先修课程
     */
    @Override
    public boolean checkPrerequisites(Integer studentId, Integer sectionId) {

        // 1. 查本节课 → courseId
        CourseSection targetSection = courseSectionMapper.findById(sectionId);
        if (targetSection == null) {
            throw new RuntimeException("选课失败：找不到课程节 sectionId=" + sectionId);
        }

        Integer courseId = targetSection.getCourseId();

        // 2. 查本课程的先修要求
        List<CoursePrereq> prereqs = coursePrereqMapper.findByCourseId(courseId);
        if (prereqs == null || prereqs.isEmpty()) {
            return true; // 没有先修要求
        }

        // 3. 查询学生已完成的课程（从 enrollment → section → course）
        List<CompletedCourseInfo> completedList = enrollmentMapper.findCompletedCourses(studentId);

        // 转成 map: key=courseId, value=grade
        Map<Integer, BigDecimal> gradeMap = completedList.stream()
                .collect(Collectors.toMap(
                        CompletedCourseInfo::getCourseId,
                        CompletedCourseInfo::getNumericGrade
                ));

        // 4. 逐条检查先修要求
        for (CoursePrereq p : prereqs) {

            Integer preCourseId = p.getPrereqCourseId();
            BigDecimal grade = gradeMap.get(preCourseId);

            // 必修课没修
            if (p.getMandatory() != null && p.getMandatory() && grade == null) {
                throw new RuntimeException(
                        "未满足必修先修课程：" + preCourseId
                );
            }

            // 有最低成绩要求但没修
            if (p.getMinGrade() != null && grade == null) {
                throw new RuntimeException(
                        "缺少达到成绩要求的先修课程：" + preCourseId
                );
            }

            // 修了但成绩不达标
            if (p.getMinGrade() != null && grade != null &&
                    grade.compareTo(p.getMinGrade()) < 0) {

                throw new RuntimeException(
                        "先修课程成绩不足：课程 " + preCourseId +
                                " 要求至少 " + p.getMinGrade() +
                                "，实际 " + grade
                );
            }
        }

        return true;
    }


    private EnrollmentResponse convertToResponse(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        EnrollmentResponse response = new EnrollmentResponse();
        BeanUtils.copyProperties(enrollment, response);

        // 设置学生信息
        if (enrollment.getStudent() != null) {
            response.setStudentNumber(enrollment.getStudent().getStudentNumber());
            response.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        }

        // 设置课程信息
        if (enrollment.getCourseSection() != null && enrollment.getCourseSection().getCourse() != null) {
            response.setCourseCode(enrollment.getCourseSection().getCourse().getCourseCode());
            response.setCourseName(enrollment.getCourseSection().getCourse().getCourseName());
            response.setSectionCode(enrollment.getCourseSection().getSectionCode());
        }

        // 设置教师信息
        if (enrollment.getCourseSection() != null && enrollment.getCourseSection().getInstructor() != null) {
            response.setInstructorName(
                    enrollment.getCourseSection().getInstructor().getFirstName() + " " +
                            enrollment.getCourseSection().getInstructor().getLastName()
            );
        }

        return response;
    }
}