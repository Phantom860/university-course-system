package com.university.university_course_system.service.impl;

import com.university.university_course_system.entity.CourseSection;
import com.university.university_course_system.mapper.CourseSectionMapper;
import com.university.university_course_system.service.CourseScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CourseScheduleServiceImpl implements CourseScheduleService {

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Override
    public boolean hasTimeConflictWithEnrolledCourses(Integer studentId, CourseSection newSection) {
        try {
            System.out.println("🔍 开始时间冲突检查...");
            System.out.println("学生ID: " + studentId);
            System.out.println("新课程: " + newSection.getSectionCode() +
                    " (" + newSection.getDaysOfWeek() + " " +
                    newSection.getStartTime() + "-" + newSection.getEndTime() + ")");
            System.out.println("学期ID: " + newSection.getSemesterId());

            if (studentId == null || newSection == null) {
                System.out.println("❌ 参数为空，跳过检查");
                return false;
            }

            // 检查课程时间信息是否完整
            if (newSection.getStartTime() == null || newSection.getEndTime() == null ||
                    newSection.getDaysOfWeek() == null || newSection.getSemesterId() == null) {
                System.out.println("❌ 课程时间信息不完整，跳过冲突检查");
                return false;
            }

            // 获取学生当前学期已选的课程 - 使用 semesterId
            Integer semesterId = newSection.getSemesterId();
            List<CourseSection> enrolledSections = getEnrolledSectionsByStudentAndSemesterId(studentId, semesterId);

            System.out.println("📊 学生已选课程数量: " + enrolledSections.size());

            if (enrolledSections.isEmpty()) {
                System.out.println("✅ 无已选课程，无时间冲突");
                return false;
            }

            // 详细显示已选课程
            System.out.println("已选课程列表:");
            for (CourseSection enrolledSection : enrolledSections) {
                System.out.println("   - " + enrolledSection.getSectionCode() +
                        " (" + enrolledSection.getDaysOfWeek() + " " +
                        enrolledSection.getStartTime() + "-" + enrolledSection.getEndTime() + ")");
            }

            // 检查与每个已选课程的冲突
            for (CourseSection enrolledSection : enrolledSections) {
                System.out.println("➡️ 检查与课程 " + enrolledSection.getSectionCode() + " 的冲突...");

                boolean hasConflict = hasTimeConflict(newSection, enrolledSection);

                if (hasConflict) {
                    System.out.println("❌ 发现时间冲突!");
                    System.out.println("   冲突课程: " + enrolledSection.getSectionCode());
                    System.out.println("   新课程: " + newSection.getSectionCode());
                    return true;
                } else {
                    System.out.println("✅ 无冲突");
                }
            }

            System.out.println("🎉 所有课程检查完成，无时间冲突");
            return false;

        } catch (Exception e) {
            System.out.println("💥 检查时间冲突时发生错误: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public List<CourseSection> getEnrolledSectionsByStudentAndSemesterId(Integer studentId, Integer semesterId) {
        return courseSectionMapper.findEnrolledSectionsByStudentAndSemesterId(studentId, semesterId);
    }

    @Override
    public boolean hasTimeConflict(CourseSection section1, CourseSection section2) {
        System.out.println("=== 详细冲突检查 ===");
        System.out.println("课程1: " + section1.getSectionCode() + " " +
                section1.getDaysOfWeek() + " " +
                section1.getStartTime() + "-" + section1.getEndTime());
        System.out.println("课程2: " + section2.getSectionCode() + " " +
                section2.getDaysOfWeek() + " " +
                section2.getStartTime() + "-" + section2.getEndTime());

        // 1. 检查学期ID
        Integer semesterId1 = section1.getSemesterId();
        Integer semesterId2 = section2.getSemesterId();
        System.out.println("学期ID检查: " + semesterId1 + " vs " + semesterId2);
        if (semesterId1 == null || semesterId2 == null || !semesterId1.equals(semesterId2)) {
            System.out.println("❌ 不同学期或无学期信息");
            return false;
        }

        // 2. 检查上课日期
        Set<DayOfWeek> commonDays = getCommonDays(section1, section2);
        System.out.println("共同上课日: " + commonDays);
        if (commonDays.isEmpty()) {
            System.out.println("❌ 无共同上课日");
            return false;
        }

        // 3. 检查时间重叠
        boolean timeOverlap = hasTimeOverlap(section1.getStartTime(), section1.getEndTime(),
                section2.getStartTime(), section2.getEndTime());
        System.out.println("时间重叠: " + timeOverlap);

        return timeOverlap;
    }

    /**
     * 获取两个课程的共同上课日
     */
    private Set<DayOfWeek> getCommonDays(CourseSection section1, CourseSection section2) {
        Set<DayOfWeek> days1 = section1.getClassDays();
        Set<DayOfWeek> days2 = section2.getClassDays();

        System.out.println("课程1上课日: " + days1);
        System.out.println("课程2上课日: " + days2);

        Set<DayOfWeek> common = new HashSet<>(days1);
        common.retainAll(days2);

        return common;
    }

    /**
     * 简化但可靠的时间重叠检查
     */
    private boolean hasTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        // 简化的逻辑：两个时间段有重叠当且仅当
        // 第一个时间段的开始时间 < 第二个时间段的结束时间 AND
        // 第二个时间段的开始时间 < 第一个时间段的结束时间
        boolean overlap = start1.isBefore(end2) && start2.isBefore(end1);

        System.out.println("时间重叠检查:");
        System.out.println("  " + start1 + " < " + end2 + ": " + start1.isBefore(end2));
        System.out.println("  " + start2 + " < " + end1 + ": " + start2.isBefore(end1));
        System.out.println("  结果: " + overlap);

        return overlap;
    }
}