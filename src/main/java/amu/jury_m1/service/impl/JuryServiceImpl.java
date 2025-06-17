package amu.jury_m1.service.impl;

import amu.jury_m1.dao.CurriculumPlanRepository;
import amu.jury_m1.dao.StudentRepository;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.model.student.Student;
import amu.jury_m1.service.ResultEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JuryServiceImpl {
    private final ResultEngine engine;
    private final CurriculumPlanRepository planRepo;
    private final StudentRepository studentRepo;

    @Transactional
    public int runJury(Long planId) {

        CurriculumPlan plan = planRepo.findById(planId)
                .orElseThrow();
        int counter = 0;

        for (Student s : studentRepo.findAll()) {
            plan.getAnnualKnowledgeBlocks().stream()
                    .flatMap(ab -> ab.getSemesters().stream())
                    .forEach(sem -> engine.recompute(s, plan, sem));
            counter++;
        }
        return counter;
    }
}
