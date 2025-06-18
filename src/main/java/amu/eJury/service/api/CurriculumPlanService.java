package amu.eJury.service.api;

import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.service.dtos.CurriculumPlanDto;

import java.util.Optional;

public interface CurriculumPlanService {
    void createCurriculumPlan(CurriculumPlanDto dto);
    void updateCurriculumPlan(Long id, CurriculumPlanDto dto);
    boolean existsDefaultCurriculumPlan();
    Optional<CurriculumPlan> findById(Long id);

}
