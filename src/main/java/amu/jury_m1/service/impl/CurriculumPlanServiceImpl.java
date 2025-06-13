package amu.jury_m1.service.impl;

import amu.jury_m1.dao.CurriculumPlanRepository;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.service.api.CurriculumPlanService;
import amu.jury_m1.service.dtos.CurriculumPlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class CurriculumPlanServiceImpl implements CurriculumPlanService {

    private final CurriculumPlanRepository curriculumPlanRepository;

    @Transactional
    public void createCurriculumPlan(CurriculumPlanDto dto) {
        CurriculumPlan plan = CurriculumPlan.builder()
                .id(1L)
                .academicYear(dto.academicYear())
                .name(dto.name())
                .build();

        curriculumPlanRepository.save(plan);
    }

    @Override
    @Transactional
    public void updateCurriculumPlan(Long id, CurriculumPlanDto dto) {
        CurriculumPlan plan = curriculumPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquette introuvable"));

        plan.setAcademicYear(dto.academicYear());
        plan.setName(dto.name());

        curriculumPlanRepository.save(plan);
    }

    @Transactional
    public boolean existsDefaultCurriculumPlan() {
        return curriculumPlanRepository.findById(1L).isPresent();
    }

    @Override
    public Optional<CurriculumPlan> findById(Long id) {
        return curriculumPlanRepository.findById(id);
    }
}
