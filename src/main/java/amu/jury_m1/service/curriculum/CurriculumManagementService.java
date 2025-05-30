package amu.jury_m1.service.curriculum;

import amu.jury_m1.dao.*;
import amu.jury_m1.domain.pedagogy.*;
import amu.jury_m1.service.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurriculumManagementService {

    private final CurriculumPlanRepository curriculumPlanRepo;
    private final TeachingUnitRepository teachingUnitRepo;
    private final KnowledgeBlockRepository knowledgeBlockRepo;
    private final UnitInBlockAssociationRepository associationRepo;

    @Transactional
    public void addTeachingUnit(String curriculumId, TeachingUnitDto dto) {
        TeachingUnit unit = TeachingUnit.builder()
                .code(dto.code())
                .label(dto.label())
                .ects(dto.ects())
                .workloadHours(dto.workloadHours())
                .build();

        teachingUnitRepo.save(unit);

        curriculumPlanRepo.findById(curriculumId).ifPresent(plan -> {
            plan.getTeachingUnits().add(unit);
            curriculumPlanRepo.save(plan);
        });
    }

    @Transactional
    public void addKnowledgeBlock(String curriculumId, KnowledgeBlockDto dto) {
        KnowledgeBlock block = KnowledgeBlock.builder()
                .code(dto.code())
                .label(dto.label())
                .semester(dto.semester())
                .ects(dto.ects())
                .build();

        knowledgeBlockRepo.save(block);

        curriculumPlanRepo.findById(curriculumId).ifPresent(plan -> {
            plan.getKnowledgeBlocks().add(block);
            curriculumPlanRepo.save(plan);
        });
    }

    @Transactional
    public void associateUnitToBlock(UnitInBlockAssociationDto dto, String curriculumId) {
        UnitInBlockId id = new UnitInBlockId(dto.teachingUnitCode(), dto.blockCode());

        UnitInBlockAssociation assoc = UnitInBlockAssociation.builder()
                .id(id)
                .coefficient(dto.coefficient())
                .build();

        associationRepo.save(assoc);

        curriculumPlanRepo.findById(curriculumId).ifPresent(plan -> {
            plan.getAssociations().add(assoc);
            curriculumPlanRepo.save(plan);
        });
    }

    @Transactional
    public void createCurriculumPlan(CurriculumPlanDto dto) {
        CurriculumPlan plan = CurriculumPlan.builder()
                .id(dto.id())
                .academicYear(dto.academicYear())
                .build();

        curriculumPlanRepo.save(plan);
    }
}