package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.service.dtos.CurriculumPlanDto;

import java.util.Optional;

public interface CurriculumPlanService {
    void createCurriculumPlan(CurriculumPlanDto dto);
    void updateCurriculumPlan(Long id, CurriculumPlanDto dto);
    boolean existsDefaultCurriculumPlan();
    Optional<CurriculumPlan> findById(Long id);

}
