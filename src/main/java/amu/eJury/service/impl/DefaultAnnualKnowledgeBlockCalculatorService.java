package amu.eJury.service.impl;

import amu.eJury.dao.AnnualKnowledgeBlockResultRepository;
import amu.eJury.dao.SemestrialKnowledgeBlockResultRepository;
import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.result.*;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.AnnualKnowledgeBlockCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultAnnualKnowledgeBlockCalculatorService implements AnnualKnowledgeBlockCalculatorService {

    private final SemestrialKnowledgeBlockResultRepository semResRepo;
    private final AnnualKnowledgeBlockResultRepository annualResRepo;

    @Transactional
    @Override
    public AnnualKnowledgeBlockResult compute(Student s, AnnualKnowledgeBlock annual) {
        SemestrialKnowledgeBlockResult s1 = semResRepo
                .findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(s, 1, annual)
                .orElse(null);
        SemestrialKnowledgeBlockResult s2 = semResRepo
                .findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(s, 2, annual)
                .orElse(null);

        ExceptionalStatus status;
        Double average = null;

        if (s1 == null || s2 == null) {
            status = ExceptionalStatus.AR;
        } else {
            status = propagate(s1.getStatus(), s2.getStatus());
            if (status == ExceptionalStatus.NONE) {
                double n1 = s1.getAverage();
                double n2 = s2.getAverage();
                double ects1 = annual.getSemesters().get(0).getEcts();
                double ects2 = annual.getSemesters().get(1).getEcts();
                average = round((n1 * ects1 + n2 * ects2) / (ects1 + ects2), 2);
            }
        }

        AnnualKnowledgeBlockResult result = annualResRepo
                .findByStudentAndAnnualBlock(s, annual)
                .orElseGet(() -> AnnualKnowledgeBlockResult.builder()
                        .student(s)
                        .annualBlock(annual)
                        .build());

        result.setStatus(status);
        result.setAverage(average);

        return annualResRepo.save(result);
    }

    /* ---------- utilitaires privés ---------- */

    private ExceptionalStatus propagate(ExceptionalStatus a, ExceptionalStatus b) {
        if (a == ExceptionalStatus.ABI || b == ExceptionalStatus.ABI) return ExceptionalStatus.ABI;
        if (a == ExceptionalStatus.ABJ || b == ExceptionalStatus.ABJ) return ExceptionalStatus.ABJ;
        if (a == ExceptionalStatus.AR  || b == ExceptionalStatus.AR)  return ExceptionalStatus.AR;
        return ExceptionalStatus.NONE;
    }

    private static double round(double v, int n) {
        return Math.round(v * Math.pow(10, n)) / Math.pow(10, n);
    }
}
