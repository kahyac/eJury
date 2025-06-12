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

    private final CurriculumPlanRepository curriculumPlanRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final SemestrialKnowledgeBlockRepository semestrialKnowledgeBlockRepository;
    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;

    @Transactional
    public void createCurriculumPlan(CurriculumPlanDto dto) {
        CurriculumPlan plan = CurriculumPlan.builder()
                .id(1L)
                .academicYear(dto.academicYear())
                .name(dto.name())
                .build();

        curriculumPlanRepository.save(plan);
    }

    @Transactional
    public void addAnnualKnowledgeBlock(AnnualKnowledgeBlockDto dto) {
        if (annualKnowledgeBlockRepository.existsById(dto.id())) {
            throw new IllegalArgumentException("Un bloc annuel avec cet identifiant existe déjà.");
        }

        AnnualKnowledgeBlock block = AnnualKnowledgeBlock.builder()
                .id(dto.id())
                .build();

        CurriculumPlan plan = curriculumPlanRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("CurriculumPlan not found"));

        plan.getAnnualKnowledgeBlocks().add(block);
        curriculumPlanRepository.save(plan);
    }

    @Transactional
    public void renameAnnualBlock(String oldId, String newId) {

        AnnualKnowledgeBlock block = annualKnowledgeBlockRepository.findById(oldId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc introuvable : " + oldId));


        if (annualKnowledgeBlockRepository.existsById(newId)) {
            throw new IllegalArgumentException("Un bloc avec ce nom existe déjà.");
        }

        AnnualKnowledgeBlock renamed = AnnualKnowledgeBlock.builder()
                .id(newId)
                .curriculumPlan(block.getCurriculumPlan())
                .semesters(block.getSemesters())
                .build();

        CurriculumPlan plan = block.getCurriculumPlan();
        plan.getAnnualKnowledgeBlocks().removeIf(b -> b.getId().equals(oldId));
        plan.getAnnualKnowledgeBlocks().add(renamed);

        curriculumPlanRepository.save(plan);
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
        if (teachingUnitRepository.existsById(dto.code())) {
            throw new IllegalArgumentException("Une UE avec ce code existe déjà.");
        }

        if (teachingUnitRepository.existsByLabel(dto.label())) {
            throw new IllegalArgumentException("Une UE avec ce libellé existe déjà.");
        }
        if (dto.ects() < 0 || dto.ects() > 30) {
            throw new IllegalArgumentException("Le nombre d’ECTS est invalide.");
        }
        if (dto.workloadHours() < 0 || dto.workloadHours() > 300) {
            throw new IllegalArgumentException("Le volume horaire est invalide.");
        }

        TeachingUnit unit = TeachingUnit.builder()
                .code(dto.code())
                .label(dto.label())
                .ects(dto.ects())
                .workloadHours(dto.workloadHours())
                .obligation(dto.obligation())
                .build();
        teachingUnitRepository.save(unit);
    }

    @Transactional
    public void updateTeachingUnit(String oldCode, TeachingUnitDto dto) {
        TeachingUnit existing = teachingUnitRepository.findById(oldCode)
                .orElseThrow(() -> new IllegalArgumentException("UE introuvable : " + oldCode));

        boolean labelConflict = teachingUnitRepository.existsByLabel(dto.label())
                && (!dto.code().equals(oldCode) || !dto.label().equals(existing.getLabel()));
        if (labelConflict) {
            throw new IllegalArgumentException("Ce libellé est déjà utilisé.");
        }
        if (!dto.code().equals(oldCode) && teachingUnitRepository.existsById(dto.code())) {
            throw new IllegalArgumentException("Ce code est déjà utilisé par une autre UE.");
        }
        if (dto.ects() < 0 || dto.ects() > 30) {
            throw new IllegalArgumentException("Le nombre d’ECTS est invalide.");
        }
        if (dto.workloadHours() < 0 || dto.workloadHours() > 300) {
            throw new IllegalArgumentException("Le volume horaire est invalide.");
        }
        if (dto.code().equals(oldCode)) {
            existing.setLabel(dto.label());
            existing.setEcts(dto.ects());
            existing.setWorkloadHours(dto.workloadHours());
            existing.setObligation(dto.obligation());
            teachingUnitRepository.save(existing);
        }
        else {
            teachingUnitRepository.delete(existing);

            TeachingUnit updated = TeachingUnit.builder()
                    .code(dto.code())
                    .label(dto.label())
                    .ects(dto.ects())
                    .workloadHours(dto.workloadHours())
                    .obligation(dto.obligation())
                    .build();

            teachingUnitRepository.save(updated);
        }
    }

    @Transactional
    public void associateTeachingUnitToSemestrialBlock(String blockCode, String unitCode, double coefficient) {
        TeachingUnit unit = teachingUnitRepository.findById(unitCode)
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

    public String findAnnualIdBySemBlockCode(String blockCode) {
        return semestrialKnowledgeBlockRepository.findById(blockCode)
                .map(sb -> sb.getAnnualKnowledgeBlock().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bloc semestriel introuvable : " + blockCode));
    }
}