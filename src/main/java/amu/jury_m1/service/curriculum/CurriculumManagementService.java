package amu.jury_m1.service.curriculum;

import amu.jury_m1.dao.*;
import amu.jury_m1.model.pedagogy.*;
import amu.jury_m1.service.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurriculumManagementService {

    private final CurriculumPlanRepository curriculumPlanRepo;
    private final TeachingUnitRepository teachingUnitRepo;
    private final SemestrialKnowledgeBlockRepository semestrialKnowledgeBlockRepository;
    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;

    @Transactional
    public void createCurriculumPlan(CurriculumPlanDto dto) {
        CurriculumPlan plan = CurriculumPlan.builder()
                .id(1L)
                .academicYear(dto.academicYear())
                .build();

        curriculumPlanRepo.save(plan);
    }

    @Transactional
    public void addAnnualKnowledgeBlock(AnnualKnowledgeBlockDto dto) {
        AnnualKnowledgeBlock block = AnnualKnowledgeBlock.builder()
                .id(dto.id())
                .build();
        annualKnowledgeBlockRepository.save(block);

        CurriculumPlan plan = curriculumPlanRepo.findById(1L)
                .orElseThrow(() -> new IllegalStateException("CurriculumPlan not found"));

        plan.getAnnualKnowledgeBlocks().add(block);
        curriculumPlanRepo.save(plan);
    }

    @Transactional
    public void addSemestrialBlockToAnnual(String annualBlockId, SemestrialKnowledgeBlockDto dto) {
        SemestrialKnowledgeBlock block = SemestrialKnowledgeBlock.builder()
                .code(dto.code())
                .label(dto.label())
                .semester(dto.semester())
                .ects(dto.ects())
                .build();
        semestrialKnowledgeBlockRepository.save(block);

        AnnualKnowledgeBlock annual = annualKnowledgeBlockRepository.findById(annualBlockId)
                .orElseThrow(() -> new IllegalArgumentException("Annual block not found: " + annualBlockId));

        annual.getSemesters().add(block);
        annualKnowledgeBlockRepository.save(annual);
    }


    @Transactional
    public void addTeachingUnit(TeachingUnitDto dto) {
        TeachingUnit unit = TeachingUnit.builder()
                .code(dto.code())
                .label(dto.label())
                .ects(dto.ects())
                .workloadHours(dto.workloadHours())
                .obligation(dto.obligation())
                .build();
        teachingUnitRepo.save(unit);
    }

    @Transactional
    public void associateTeachingUnitToSemestrialBlock(String blockCode, String unitCode, double coefficient) {
        TeachingUnit unit = teachingUnitRepo.findById(unitCode)
                .orElseThrow(() -> new IllegalArgumentException("TeachingUnit not found: " + unitCode));

        SemestrialKnowledgeBlock block = semestrialKnowledgeBlockRepository.findById(blockCode)
                .orElseThrow(() -> new IllegalArgumentException("Semestrial block not found: " + blockCode));

        block.getUnitsCoefficientAssociation().put(unit, coefficient);
        semestrialKnowledgeBlockRepository.save(block);
    }
}