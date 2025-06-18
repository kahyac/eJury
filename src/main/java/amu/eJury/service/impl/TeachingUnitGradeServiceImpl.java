package amu.eJury.service.impl;

import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.result.ExceptionalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeachingUnitGradeServiceImpl {

    public String getDisplayValue(TeachingUnitGrade gradeInfo) {
        if (gradeInfo == null) return "-";

        if (gradeInfo.getStatus() == ExceptionalStatus.NONE && gradeInfo.getGrade() != null) {
            return String.format("%.1f / 20", gradeInfo.getGrade());
        } else {
            return gradeInfo.getStatus().name();
        }
    }
}
