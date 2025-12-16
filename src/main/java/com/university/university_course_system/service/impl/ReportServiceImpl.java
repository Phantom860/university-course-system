package com.university.university_course_system.service.impl;

import com.university.university_course_system.dto.response.CourseGradeDistributionDTO;
import com.university.university_course_system.dto.response.GradeStatisticsDTO;
import com.university.university_course_system.dto.response.StudentGpaDTO;
import com.university.university_course_system.entity.Course;
import com.university.university_course_system.entity.Department;
import com.university.university_course_system.entity.Enrollment;
import com.university.university_course_system.entity.Student;
import com.university.university_course_system.mapper.*;
import com.university.university_course_system.service.ReportService;
import com.university.university_course_system.util.GradeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Autowired
    private DepartmentMapper departmentMapper;


    /*
    获取学生gpa
     */
    @Override
    public StudentGpaDTO getStudentGpa(Integer studentId) {
        try {
            // 获取学生的所有已修课程
            var enrollments = enrollmentMapper.findByStudentId(studentId);

            var passedCourses = enrollments.stream()
                    .filter(e -> e.getEnrollmentStatus() != null && "passed".equals(e.getEnrollmentStatus().name()))
                    .collect(Collectors.toList());

            // 计算总学分和GPA
            BigDecimal totalCredits = BigDecimal.ZERO;
            BigDecimal totalGradePoints = BigDecimal.ZERO;

            for (var enrollment : passedCourses) {
                // 添加空值检查
                if (enrollment != null &&
                        enrollment.getGradePoints() != null &&
                        enrollment.getCourseSection() != null &&
                        enrollment.getCourseSection().getCourse() != null &&
                        enrollment.getCourseSection().getCourse().getCredits() != null) {

                    BigDecimal credits = enrollment.getCourseSection().getCourse().getCredits();
                    BigDecimal gradePoints = enrollment.getGradePoints();

                    // 确保数值有效
                    if (credits != null && gradePoints != null) {
                        totalCredits = totalCredits.add(credits);
                        totalGradePoints = totalGradePoints.add(credits.multiply(gradePoints));
                    }
                }
            }

            BigDecimal gpa = totalCredits != null && totalCredits.compareTo(BigDecimal.ZERO) > 0
                    ? totalGradePoints.divide(totalCredits, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            StudentGpaDTO dto = new StudentGpaDTO();
            dto.setTotalCourses(passedCourses.size());
            dto.setTotalCredits(totalCredits != null ? totalCredits : BigDecimal.ZERO);
            dto.setCumulativeGpa(gpa);

            // 设置学生信息
            var student = studentMapper.findByIdWithDepartment(studentId);
            if (student != null) {
                dto.setStudentNumber(student.getStudentNumber());
                dto.setStudentName(student.getFirstName() + " " + student.getLastName());
                if (student.getDepartment() != null) {
                    dto.setDepartmentName(student.getDepartment().getDepartmentName());
                }
            }

            return dto;
        } catch (Exception e) {
            // 返回默认值而不是抛出异常
            StudentGpaDTO dto = new StudentGpaDTO();
            dto.setTotalCourses(0);
            dto.setTotalCredits(BigDecimal.ZERO);
            dto.setCumulativeGpa(BigDecimal.ZERO);

            var student = studentMapper.findByIdWithDepartment(studentId);
            if (student != null) {
                dto.setStudentNumber(student.getStudentNumber());
                dto.setStudentName(student.getFirstName() + " " + student.getLastName());
                if (student.getDepartment() != null) {
                    dto.setDepartmentName(student.getDepartment().getDepartmentName());
                }
            }
            return dto;
        }
    }


    /**
     * 获取优秀学生
     * @param topN
     * @return
     */
    @Override
    public List<StudentGpaDTO> getTopStudentsByGpa(Integer topN) {
        try {
            // 获取所有学生计算GPA后排序
            var allStudents = studentMapper.findAllWithDepartment();

            return allStudents.stream()
                    .map(student -> {
                        try {
                            return getStudentGpa(student.getStudentId());
                        } catch (Exception e) {
                            // 如果计算GPA出错，返回默认值
                            StudentGpaDTO dto = new StudentGpaDTO();
                            dto.setStudentNumber(student.getStudentNumber());
                            dto.setStudentName(student.getFirstName() + " " + student.getLastName());
                            if (student.getDepartment() != null) {
                                dto.setDepartmentName(student.getDepartment().getDepartmentName());
                            }
                            dto.setTotalCourses(0);
                            dto.setTotalCredits(BigDecimal.ZERO);
                            dto.setCumulativeGpa(BigDecimal.ZERO);
                            return dto;
                        }
                    })
                    .filter(dto -> dto.getCumulativeGpa() != null && dto.getCumulativeGpa().compareTo(BigDecimal.ZERO) > 0)
                    .sorted((s1, s2) -> s2.getCumulativeGpa().compareTo(s1.getCumulativeGpa()))
                    .limit(topN)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取优秀学生失败: " + e.getMessage(), e);
        }
    }


    /**
     * 所有课程成绩统计
     * @return
     */
    @Override
    public List<GradeStatisticsDTO> getCourseGradeStatistics() {
        // 获取所有课程并计算统计信息
        var courses = courseMapper.findAllWithDepartment();

        return courses.stream()
                .map(course -> getCourseGradeStatisticsByCourse(course.getCourseId()))
                .collect(Collectors.toList());
    }


    /**
     * 单个课程成绩统计
     * @param courseId
     * @return
     */
    @Override
    public GradeStatisticsDTO getCourseGradeStatisticsByCourse(Integer courseId) {
        // 获取课程的所有课程段ID
        List<Integer> courseSections = courseSectionMapper.findSectionIdsByCourseId(courseId);

        // 获取所有选课记录
        List<Object> allEnrollments = new ArrayList<>();
        for (Integer sectionId : courseSections) {
            var enrollments = enrollmentMapper.findBySectionId(sectionId);
            allEnrollments.addAll(enrollments);
        }

        var passedEnrollments = allEnrollments.stream()
                .filter(e -> ((com.university.university_course_system.entity.Enrollment) e).getEnrollmentStatus() != null
                        && ("passed".equals(((com.university.university_course_system.entity.Enrollment) e).getEnrollmentStatus().name())
                        || "failed".equals(((com.university.university_course_system.entity.Enrollment) e).getEnrollmentStatus().name())))
                .collect(Collectors.toList());

        var gradeEnrollments = passedEnrollments.stream()
                .filter(e -> ((com.university.university_course_system.entity.Enrollment) e).getNumericGrade() != null)
                .collect(Collectors.toList());

        // 计算统计信息
        GradeStatisticsDTO dto = new GradeStatisticsDTO();
        dto.setTotalStudents(passedEnrollments.size());
        dto.setPassedStudents((int) passedEnrollments.stream()
                .filter(e -> "passed".equals(((com.university.university_course_system.entity.Enrollment) e).getEnrollmentStatus().name()))
                .count());
        dto.setFailedStudents(dto.getTotalStudents() - dto.getPassedStudents());

        // 计算通过率
        if (dto.getTotalStudents() > 0) {
            BigDecimal passRate = new BigDecimal(dto.getPassedStudents())
                    .divide(new BigDecimal(dto.getTotalStudents()), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            dto.setPassRate(passRate);
        } else {
            dto.setPassRate(BigDecimal.ZERO);
        }

        // 计算平均分、最高分、最低分
        if (!gradeEnrollments.isEmpty()) {
            var grades = gradeEnrollments.stream()
                    .map(e -> ((com.university.university_course_system.entity.Enrollment) e).getNumericGrade())
                    .collect(Collectors.toList());

            BigDecimal sum = grades.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setAverageGrade(sum.divide(new BigDecimal(grades.size()), 2, RoundingMode.HALF_UP));
            dto.setMaxGrade(grades.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            dto.setMinGrade(grades.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
        } else {
            dto.setAverageGrade(BigDecimal.ZERO);
            dto.setMaxGrade(BigDecimal.ZERO);
            dto.setMinGrade(BigDecimal.ZERO);
        }

        // 设置课程信息
        var course = courseMapper.findByIdWithDepartment(courseId);
        if (course != null) {
            dto.setCourseCode(course.getCourseCode());
            dto.setCourseName(course.getCourseName());
        }

        return dto;
    }


    /**
     * 课程成绩分布
     * @param courseId
     * @return
     */
    @Override
    public CourseGradeDistributionDTO getCourseGradeDistribution(Integer courseId) {
        CourseGradeDistributionDTO dto = new CourseGradeDistributionDTO();

        // 设置课程信息
        var course = courseMapper.findByIdWithDepartment(courseId);
        if (course != null) {
            dto.setCourseCode(course.getCourseCode());
            dto.setCourseName(course.getCourseName());
        }

        try {
            // 1. 获取课程的所有选课记录
            List<Integer> courseSections = courseSectionMapper.findSectionIdsByCourseId(courseId);
            List<Enrollment> allEnrollments = new ArrayList<>();

            for (Integer sectionId : courseSections) {
                var enrollments = enrollmentMapper.findBySectionId(sectionId);
                allEnrollments.addAll(enrollments);
            }

            // 2. 过滤出有成绩的记录
            var gradeEnrollments = allEnrollments.stream()
                    .filter(e -> e.getNumericGrade() != null)
                    .collect(Collectors.toList());

            // 3. 使用GradeCalculator统一计算分布
            Map<String, Integer> gradeDistribution = calculateGradeDistribution(gradeEnrollments);
            Map<String, Integer> scoreDistribution = calculateScoreDistribution(gradeEnrollments);

            dto.setGradeDistribution(gradeDistribution);
            dto.setScoreDistribution(scoreDistribution);

        } catch (Exception e) {
            System.err.println("获取成绩分布失败: " + e.getMessage());
            dto.setGradeDistribution(createEmptyGradeDistribution());
            dto.setScoreDistribution(createEmptyScoreDistribution());
        }

        return dto;
    }

    // 使用GradeCalculator计算字母等级分布
    private Map<String, Integer> calculateGradeDistribution(List<Enrollment> enrollments) {
        Map<String, Integer> distribution = createEmptyGradeDistribution();

        for (Enrollment enrollment : enrollments) {
            String letterGrade = GradeCalculator.calculateLetterGrade(enrollment.getNumericGrade());
            distribution.merge(letterGrade, 1, Integer::sum);
        }

        return distribution;
    }

    // 计算分数段分布
    private Map<String, Integer> calculateScoreDistribution(List<Enrollment> enrollments) {
        Map<String, Integer> distribution = createEmptyScoreDistribution();

        for (Enrollment enrollment : enrollments) {
            BigDecimal grade = enrollment.getNumericGrade();
            if (grade.compareTo(new BigDecimal("90")) >= 0) {
                distribution.merge("90-100", 1, Integer::sum);
            } else if (grade.compareTo(new BigDecimal("80")) >= 0) {
                distribution.merge("80-89", 1, Integer::sum);
            } else if (grade.compareTo(new BigDecimal("70")) >= 0) {
                distribution.merge("70-79", 1, Integer::sum);
            } else if (grade.compareTo(new BigDecimal("60")) >= 0) {
                distribution.merge("60-69", 1, Integer::sum);
            } else {
                distribution.merge("0-59", 1, Integer::sum);
            }
        }

        return distribution;
    }

    // 创建空的字母等级分布
    private Map<String, Integer> createEmptyGradeDistribution() {
        Map<String, Integer> empty = new LinkedHashMap<>();
        String[] grades = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
        for (String grade : grades) {
            empty.put(grade, 0);
        }
        return empty;
    }

    // 创建空的分数段分布
    private Map<String, Integer> createEmptyScoreDistribution() {
        Map<String, Integer> empty = new LinkedHashMap<>();
        String[] scoreRanges = {"90-100", "80-89", "70-79", "60-69", "0-59"};
        for (String range : scoreRanges) {
            empty.put(range, 0);
        }
        return empty;
    }


    /**
     * 根据院系id查看院系学生gpa
     * @param departmentId
     * @return
     */
    @Override
    public List<StudentGpaDTO> getDepartmentStudentsGpa(Integer departmentId) {
        try {
            // 验证院系是否存在
            var department = getDepartmentById(departmentId);
            if (department == null) {
                throw new RuntimeException("院系不存在，ID: " + departmentId);
            }

            // 1. 获取院系下的所有学生
            List<Student> departmentStudents = studentMapper.findByDepartmentId(departmentId);

            // 2. 为每个学生计算GPA
            List<StudentGpaDTO> studentGpas = departmentStudents.stream()
                    .map(student -> {
                        try {
                            return calculateStudentGpa(student);
                        } catch (Exception e) {
                            // 如果计算失败，返回基础信息
                            return createBasicStudentGpaDTO(student);
                        }
                    })
                    .collect(Collectors.toList());

            // 3. 按GPA降序排序
            studentGpas.sort((s1, s2) -> s2.getCumulativeGpa().compareTo(s1.getCumulativeGpa()));

            return studentGpas;

        } catch (Exception e) {
            throw new RuntimeException("获取院系学生GPA失败: " + e.getMessage(), e);
        }
    }

    // 计算学生GPA的专用方法（复用getStudentGpa的逻辑但返回StudentGpaDTO）
    private StudentGpaDTO calculateStudentGpa(Student student) {
        // 获取学生的所有已修课程
        var enrollments = enrollmentMapper.findByStudentId(student.getStudentId());

        var passedCourses = enrollments.stream()
                .filter(e -> e.getEnrollmentStatus() != null && "passed".equals(e.getEnrollmentStatus().name()))
                .collect(Collectors.toList());

        // 计算总学分和GPA
        BigDecimal totalCredits = BigDecimal.ZERO;
        BigDecimal totalGradePoints = BigDecimal.ZERO;

        for (var enrollment : passedCourses) {
            // 空值检查
            if (enrollment != null &&
                    enrollment.getGradePoints() != null &&
                    enrollment.getCourseSection() != null &&
                    enrollment.getCourseSection().getCourse() != null &&
                    enrollment.getCourseSection().getCourse().getCredits() != null) {

                BigDecimal credits = enrollment.getCourseSection().getCourse().getCredits();
                BigDecimal gradePoints = enrollment.getGradePoints();

                if (credits != null && gradePoints != null) {
                    totalCredits = totalCredits.add(credits);
                    totalGradePoints = totalGradePoints.add(credits.multiply(gradePoints));
                }
            }
        }

        // 计算GPA
        BigDecimal gpa = totalCredits.compareTo(BigDecimal.ZERO) > 0
                ? totalGradePoints.divide(totalCredits, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // 创建并返回DTO
        StudentGpaDTO dto = new StudentGpaDTO();
        dto.setStudentNumber(student.getStudentNumber());
        dto.setStudentName(student.getFirstName() + " " + student.getLastName());
        dto.setDepartmentName(student.getDepartment() != null ? student.getDepartment().getDepartmentName() : null);
        dto.setTotalCourses(passedCourses.size());
        dto.setTotalCredits(totalCredits);
        dto.setCumulativeGpa(gpa);

        return dto;
    }

    // 创建基础学生GPA信息（当计算失败时使用）
    private StudentGpaDTO createBasicStudentGpaDTO(Student student) {
        StudentGpaDTO dto = new StudentGpaDTO();
        dto.setStudentNumber(student.getStudentNumber());
        dto.setStudentName(student.getFirstName() + " " + student.getLastName());
        dto.setDepartmentName(student.getDepartment() != null ? student.getDepartment().getDepartmentName() : null);
        dto.setTotalCourses(0);
        dto.setTotalCredits(BigDecimal.ZERO);
        dto.setCumulativeGpa(BigDecimal.ZERO);
        return dto;
    }

    // 辅助方法：根据ID获取院系
    private Department getDepartmentById(Integer departmentId) {
        return departmentMapper.findById(departmentId);
    }


    /**
     * 根据院系id查看课程成绩
     * @param departmentId
     * @return
     */
    @Override
    public List<GradeStatisticsDTO> getDepartmentCourseStatistics(Integer departmentId) {
        try {
            // 1. 验证院系是否存在
            var department = getDepartmentById(departmentId);
            if (department == null) {
                throw new RuntimeException("院系不存在，ID: " + departmentId);
            }

            // 2. 从Mapper获取统计数据
            List<GradeStatisticsDTO> statistics = courseMapper.findCourseStatisticsByDepartment(departmentId);

            // 3. 处理空值和格式化
            return statistics.stream()
                    .map(this::processGradeStatistics)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("获取院系课程统计失败: " + e.getMessage(), e);
        }
    }

    // 处理统计数据的辅助方法
    private GradeStatisticsDTO processGradeStatistics(GradeStatisticsDTO stats) {
        if (stats == null) {
            return createEmptyGradeStatistics();
        }

        // 确保数值不为null
        if (stats.getTotalStudents() == null) stats.setTotalStudents(0);
        if (stats.getPassedStudents() == null) stats.setPassedStudents(0);
        if (stats.getFailedStudents() == null) stats.setFailedStudents(0);
        if (stats.getPassRate() == null) stats.setPassRate(BigDecimal.ZERO);
        if (stats.getAverageGrade() == null) stats.setAverageGrade(BigDecimal.ZERO);
        if (stats.getMaxGrade() == null) stats.setMaxGrade(BigDecimal.ZERO);
        if (stats.getMinGrade() == null) stats.setMinGrade(BigDecimal.ZERO);

        return stats;
    }

    // 创建空的课程统计
    private GradeStatisticsDTO createEmptyGradeStatistics() {
        GradeStatisticsDTO stats = new GradeStatisticsDTO();
        stats.setTotalStudents(0);
        stats.setPassedStudents(0);
        stats.setFailedStudents(0);
        stats.setPassRate(BigDecimal.ZERO);
        stats.setAverageGrade(BigDecimal.ZERO);
        stats.setMaxGrade(BigDecimal.ZERO);
        stats.setMinGrade(BigDecimal.ZERO);
        return stats;
    }

    // 验证院系是否存在
    private boolean isDepartmentExists(Integer departmentId) {
        try {
            // 如果有DepartmentMapper，使用它
            // return departmentMapper.findById(departmentId) != null;

            // 临时方案：查询该院系下是否有课程
            List<Course> courses = courseMapper.findByDepartmentId(departmentId);
            return !courses.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 计算院系平均gpa
     * @param departmentId
     * @return
     */
    @Override
    public BigDecimal getDepartmentAverageGpa(Integer departmentId) {
        try {
            // 验证院系是否存在
            var department = getDepartmentById(departmentId);
            if (department == null) {
                throw new RuntimeException("院系不存在，ID: " + departmentId);
            }

            // 使用现有的getDepartmentStudentsGpa方法获取实时GPA数据
            List<StudentGpaDTO> students = getDepartmentStudentsGpa(departmentId);

            // 过滤出有有效GPA的学生
            List<BigDecimal> validGpas = students.stream()
                    .filter(student -> student.getCumulativeGpa() != null
                            && student.getCumulativeGpa().compareTo(BigDecimal.ZERO) > 0)
                    .map(StudentGpaDTO::getCumulativeGpa)
                    .collect(Collectors.toList());

            // 计算平均GPA
            if (validGpas.isEmpty()) {
                return BigDecimal.ZERO;
            }

            BigDecimal totalGpa = validGpas.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return totalGpa.divide(
                    new BigDecimal(validGpas.size()),
                    2,
                    RoundingMode.HALF_UP
            );

        } catch (RuntimeException e) {
            // 业务异常在Controller层处理
            throw e;
        } catch (Exception e) {
            System.err.println("计算院系平均GPA失败: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }


    /**
     * 获取课程通过率
     * @param courseId
     * @return
     */
    @Override
    public Integer getCoursePassRate(Integer courseId) {
        try {
            // 1. 验证课程是否存在
            var course = courseMapper.findByIdWithDepartment(courseId);
            if (course == null) {
                throw new RuntimeException("课程不存在，ID: " + courseId);
            }

            // 2. 获取课程的所有选课记录
            List<Integer> courseSections = courseSectionMapper.findSectionIdsByCourseId(courseId);
            int totalStudents = 0;
            int passedStudents = 0;

            for (Integer sectionId : courseSections) {
                var enrollments = enrollmentMapper.findBySectionId(sectionId);

                // 过滤出有最终状态的选课记录（passed或failed）
                var finalEnrollments = enrollments.stream()
                        .filter(e -> e.getEnrollmentStatus() != null
                                && ("passed".equals(e.getEnrollmentStatus().name())
                                || "failed".equals(e.getEnrollmentStatus().name())))
                        .collect(Collectors.toList());

                totalStudents += finalEnrollments.size();
                passedStudents += (int) finalEnrollments.stream()
                        .filter(e -> "passed".equals(e.getEnrollmentStatus().name()))
                        .count();
            }

            // 3. 计算通过率
            if (totalStudents == 0) {
                return 0; // 没有学生选课或没有最终成绩
            }

            double passRate = (passedStudents * 100.0) / totalStudents;
            return (int) Math.round(passRate); // 返回整数百分比

        } catch (RuntimeException e) {
            // 业务异常在Controller层处理
            throw e;
        } catch (Exception e) {
            System.err.println("计算课程通过率失败，课程ID: " + courseId + ", 错误: " + e.getMessage());
            return 0;
        }
    }
}