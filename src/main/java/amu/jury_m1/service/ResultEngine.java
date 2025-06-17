package amu.jury_m1.service;

import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.model.pedagogy.SemestrialKnowledgeBlock;
import amu.jury_m1.model.student.Student;

import amu.jury_m1.service.api.AnnualCalculator;
import amu.jury_m1.service.api.SemestrialCalculator;
import amu.jury_m1.service.api.YearDecisionCalculator;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResultEngine {

    private final SemestrialCalculator  semCalc;  // existe déjà
    private final AnnualCalculator      annCalc;  // implémenté ci-dessus
    private final YearDecisionCalculator yearCalc; // idem

    @Transactional
    public void recompute(Student s, CurriculumPlan plan,
                          SemestrialKnowledgeBlock updatedBlock) {

        semCalc.compute(s, updatedBlock);
        annCalc.compute(s, updatedBlock.getAnnualKnowledgeBlock());
        yearCalc.compute(s, plan);
    }
}