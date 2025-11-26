package com.university.university_course_system.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GradeUpdateRequest {
    private BigDecimal numericGrade;
    private String letterGrade;

    public BigDecimal getNumericGrade() {
        return numericGrade;
    }

    public void setNumericGrade(BigDecimal numericGrade) {
        this.numericGrade = numericGrade;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }
}
