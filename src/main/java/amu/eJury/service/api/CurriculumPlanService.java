package amu.eJury.service.api;

import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.service.dtos.CurriculumPlanDTO;

import java.util.Optional;

public interface CurriculumPlanService {
    void createCurriculumPlan(CurriculumPlanDTO dto);
    void updateCurriculumPlan(Long id, CurriculumPlanDTO dto);
    boolean existsDefaultCurriculumPlan();
    Optional<CurriculumPlan> findById(Long id);

}
