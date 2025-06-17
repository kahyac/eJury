package amu.jury_m1.service.impl;

import amu.jury_m1.dao.AnnualBlockResultRepository;
import amu.jury_m1.dao.AcademicYearResultRepository;
import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.model.result.*;
import amu.jury_m1.model.student.Student;
import amu.jury_m1.service.api.AnnualCalculator;
import amu.jury_m1.service.api.YearDecisionCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultYearDecisionCalculator implements YearDecisionCalculator {

    private final AnnualCalculator annualCalc;                 // réutilise l’algo précédent
    private final AnnualBlockResultRepository annualResRepo;
    private final AcademicYearResultRepository yearResRepo;

    @Transactional
    @Override
    public AcademicYearResult compute(Student s, CurriculumPlan plan) {

        boolean exceptional = false;
        int failures = 0;
        int almost   = 0;

        /* boucle sur les blocs annuels de la maquette */
        for (AnnualKnowledgeBlock ab : plan.getAnnualKnowledgeBlocks()) {

            AnnualBlockResult r = annualCalc.compute(s, ab);   // (re)calcule et sauvegarde

            if (r.getStatus() != ExceptionalStatus.NONE) {     // ABI / ABJ / AR → décision miroir
                exceptional = true;
                break;
            }

            double m = r.getAverage();
            if (m < 9.5) failures++;
            else if (m < 10) almost++;
        }

        AcademicDecision decision;
        if (exceptional) {
            decision = AcademicDecision.valueOf(   // on reflète le statut bloquant
                    worstExceptionalStatus(s, plan).name());
        } else if (failures == 0 && almost <= 1) {
            decision = AcademicDecision.ADM;
        } else {
            decision = AcademicDecision.AJ;
        }

        return yearResRepo.save(
                AcademicYearResult.builder()
                        .student(s)
                        .academicYear(plan.getAcademicYear())
                        .decision(decision)
                        .build()
        );
    }

    /* ----------- statut exceptionnel le plus sévère ----------- */
    private ExceptionalStatus worstExceptionalStatus(Student s, CurriculumPlan plan) {
        return plan.getAnnualKnowledgeBlocks().stream()
                .map(ab -> annualResRepo.findByStudentAndAnnualBlock(s, ab)
                        .map(AnnualBlockResult::getStatus)
                        .orElse(ExceptionalStatus.NONE))
                .reduce(ExceptionalStatus.NONE, this::propagate);
    }

    private ExceptionalStatus propagate(ExceptionalStatus a, ExceptionalStatus b) {
        if (a == ExceptionalStatus.ABI || b == ExceptionalStatus.ABI) return ExceptionalStatus.ABI;
        if (a == ExceptionalStatus.ABJ || b == ExceptionalStatus.ABJ) return ExceptionalStatus.ABJ;
        if (a == ExceptionalStatus.AR  || b == ExceptionalStatus.AR)  return ExceptionalStatus.AR;
        return ExceptionalStatus.NONE;
    }
}
