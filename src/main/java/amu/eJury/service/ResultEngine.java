package amu.eJury.service;

import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.student.Student;

import amu.eJury.service.api.AnnualCalculator;
import amu.eJury.service.api.SemestrialCalculator;
import amu.eJury.service.api.YearDecisionCalculator;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResultEngine {

    private final SemestrialCalculator  semCalc;
    private final AnnualCalculator      annCalc;
    private final YearDecisionCalculator yearCalc;

    @Transactional
    public void recompute(Student s, CurriculumPlan plan,
                          SemestrialKnowledgeBlock updatedBlock) {

        semCalc.compute(s, updatedBlock);
        annCalc.compute(s, updatedBlock.getAnnualKnowledgeBlock());
        yearCalc.compute(s, plan);
    }
}