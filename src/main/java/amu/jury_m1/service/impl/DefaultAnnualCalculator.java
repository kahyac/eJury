package amu.jury_m1.service.impl;

import amu.jury_m1.dao.AnnualBlockResultRepository;
import amu.jury_m1.dao.SemestrialBlockResultRepository;
import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.result.*;
import amu.jury_m1.model.student.Student;
import amu.jury_m1.service.api.AnnualCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultAnnualCalculator implements AnnualCalculator {

    private final SemestrialBlockResultRepository semResRepo;
    private final AnnualBlockResultRepository annualResRepo;

    @Transactional
    @Override
    public AnnualBlockResult compute(Student s, AnnualKnowledgeBlock annual) {

        /* Récupère les deux résultats semestriels déjà calculés */
        SemestrialBlockResult s1 = semResRepo
                .findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(s, 1, annual)
                .orElseThrow(() -> new IllegalStateException("Result S1 manquant"));
        SemestrialBlockResult s2 = semResRepo
                .findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(s, 2, annual)
                .orElseThrow(() -> new IllegalStateException("Result S2 manquant"));

        /* Propagation du statut exceptionnel le plus sévère */
        ExceptionalStatus st = propagate(s1.getStatus(), s2.getStatus());
        if (st != ExceptionalStatus.NONE) {
            return annualResRepo.save(
                    AnnualBlockResult.builder()
                            .student(s).annualBlock(annual)
                            .status(st).average(null)
                            .build()
            );
        }

        /* Pondération par les ECTS des deux semestres */
        double n1 = s1.getAverage();
        double n2 = s2.getAverage();
        double ects1 = annual.getSemesters().get(0).getEcts();
        double ects2 = annual.getSemesters().get(1).getEcts();

        double avg = round((n1 * ects1 + n2 * ects2) / (ects1 + ects2), 2);

        return annualResRepo.save(
                AnnualBlockResult.builder()
                        .student(s).annualBlock(annual)
                        .status(ExceptionalStatus.NONE)
                        .average(avg)
                        .build()
        );
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
