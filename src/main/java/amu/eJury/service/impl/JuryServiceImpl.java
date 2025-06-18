package amu.eJury.service.impl;

import amu.eJury.dao.CurriculumPlanRepository;
import amu.eJury.dao.StudentRepository;
import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.model.student.Student;
import amu.eJury.service.ResultEngine;
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
