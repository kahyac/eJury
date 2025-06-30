package amu.eJury.service;

import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.users.Student;

import amu.eJury.service.api.AnnualKnowledgeBlockCalculatorService;
import amu.eJury.service.api.SemestrialKnowledgeBlockCalculatorService;
import amu.eJury.service.api.AcademicYearDecisionCalculatorService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResultEngine {

    private final SemestrialKnowledgeBlockCalculatorService semCalc;
    private final AnnualKnowledgeBlockCalculatorService annCalc;
    private final AcademicYearDecisionCalculatorService yearCalc;

    @Transactional
    public void recompute(Student s, CurriculumPlan plan,
                          SemestrialKnowledgeBlock updatedBlock) {

        semCalc.compute(s, updatedBlock);
        annCalc.compute(s, updatedBlock.getAnnualKnowledgeBlock());
        yearCalc.compute(s, plan);
    }
}