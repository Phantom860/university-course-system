package com.university.university_course_system.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GradeCalculator {

    /**
     * 根据百分制分数计算绩点
     * 标准4.0绩点系统：
     * 90-100: 4.0, 85-89: 3.7, 82-84: 3.3, 78-81: 3.0
     * 75-77: 2.7, 72-74: 2.3, 68-71: 2.0, 64-67: 1.5
     * 60-63: 1.0, 0-59: 0.0
     */
    public static BigDecimal calculateGradePoints(BigDecimal numericGrade) {
        if (numericGrade == null) {
            return BigDecimal.ZERO;
        }

        double grade = numericGrade.doubleValue();

        if (grade >= 90) return new BigDecimal("4.0");
        else if (grade >= 85) return new BigDecimal("3.7");
        else if (grade >= 82) return new BigDecimal("3.3");
        else if (grade >= 78) return new BigDecimal("3.0");
        else if (grade >= 75) return new BigDecimal("2.7");
        else if (grade >= 72) return new BigDecimal("2.3");
        else if (grade >= 68) return new BigDecimal("2.0");
        else if (grade >= 64) return new BigDecimal("1.5");
        else if (grade >= 60) return new BigDecimal("1.0");
        else return BigDecimal.ZERO;
    }

    /**
     * 根据百分制分数计算字母等级
     */
    public static String calculateLetterGrade(BigDecimal numericGrade) {
        if (numericGrade == null) {
            return "F";
        }

        double grade = numericGrade.doubleValue();

        if (grade >= 90) return "A";
        else if (grade >= 85) return "A-";
        else if (grade >= 82) return "B+";
        else if (grade >= 78) return "B";
        else if (grade >= 75) return "B-";
        else if (grade >= 72) return "C+";
        else if (grade >= 68) return "C";
        else if (grade >= 64) return "C-";
        else if (grade >= 60) return "D";
        else return "F";
    }

    /**
     * 计算加权平均绩点(GPA)
     */
    public static BigDecimal calculateGPA(BigDecimal totalGradePoints, BigDecimal totalCredits) {
        if (totalCredits == null || totalCredits.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalGradePoints.divide(totalCredits, 2, RoundingMode.HALF_UP);
    }

    /**
     * 根据字母等级计算绩点
     */
    public static BigDecimal calculateGradePointsFromLetter(String letterGrade) {
        if (letterGrade == null) {
            return BigDecimal.ZERO;
        }

        switch (letterGrade.toUpperCase()) {
            case "A": return new BigDecimal("4.0");
            case "A-": return new BigDecimal("3.7");
            case "B+": return new BigDecimal("3.3");
            case "B": return new BigDecimal("3.0");
            case "B-": return new BigDecimal("2.7");
            case "C+": return new BigDecimal("2.3");
            case "C": return new BigDecimal("2.0");
            case "C-": return new BigDecimal("1.5");
            case "D": return new BigDecimal("1.0");
            case "F": return BigDecimal.ZERO;
            default: return BigDecimal.ZERO;
        }
    }
}
