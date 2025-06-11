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
                .name(dto.name())
                .build();

        curriculumPlanRepo.save(plan);
    }

    @Transactional
    public void addAnnualKnowledgeBlock(AnnualKnowledgeBlockDto dto) {
        if (annualKnowledgeBlockRepository.existsById(dto.id())) {
            throw new IllegalArgumentException("Un bloc annuel avec cet identifiant existe déjà.");
        }

        AnnualKnowledgeBlock block = AnnualKnowledgeBlock.builder()
                .id(dto.id())
                .build();

        CurriculumPlan plan = curriculumPlanRepo.findById(1L)
                .orElseThrow(() -> new IllegalStateException("CurriculumPlan not found"));

        plan.getAnnualKnowledgeBlocks().add(block);
        curriculumPlanRepo.save(plan);
    }


    @Transactional
    public void addSemestrialBlockToAnnual(String annualBlockId, SemestrialKnowledgeBlockDto dto) {
        AnnualKnowledgeBlock annual = annualKnowledgeBlockRepository.findById(annualBlockId)
                .orElseThrow(() -> new IllegalArgumentException("Annual block not found: " + annualBlockId));

        SemestrialKnowledgeBlock block = SemestrialKnowledgeBlock.builder()
                .code(dto.code())
                .label(dto.label())
                .semester(dto.semester())
                .ects(dto.ects())
                .annualKnowledgeBlock(annual)
                .build();

        semestrialKnowledgeBlockRepository.save(block);

        annual.getSemesters().add(block);
        annualKnowledgeBlockRepository.save(annual);
    }



    @Transactional
    public void addTeachingUnit(TeachingUnitDto dto) {
        if (teachingUnitRepo.existsById(dto.code())) {
            throw new IllegalArgumentException("Une UE avec ce code existe déjà.");
        }

        if (teachingUnitRepo.existsByLabel(dto.label())) {
            throw new IllegalArgumentException("Une UE avec ce libellé existe déjà.");
        }

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

    public Long findCurriculumIdBySemBlockCode(String blockCode) {
        return semestrialKnowledgeBlockRepository.findById(blockCode)
                .map(sb -> sb.getAnnualKnowledgeBlock().getCurriculumPlan().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bloc semestriel introuvable"));
    }
}