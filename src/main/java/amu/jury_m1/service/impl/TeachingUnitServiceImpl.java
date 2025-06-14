package amu.jury_m1.service.impl;

import amu.jury_m1.dao.TeachingUnitRepository;
import amu.jury_m1.model.pedagogy.SemestrialKnowledgeBlock;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.service.api.TeachingUnitService;
import amu.jury_m1.service.dtos.TeachingUnitDto;
import amu.jury_m1.service.refactor.TeachingUnitRefactorService;
import amu.jury_m1.service.validator.TeachingUnitValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeachingUnitServiceImpl implements TeachingUnitService {

    private final TeachingUnitRepository teachingUnitRepository;
    private final TeachingUnitValidator teachingUnitValidator;
    private final TeachingUnitRefactorService teachingUnitRefactorService;

    @Transactional
    public void addTeachingUnit(TeachingUnitDto dto) {
        teachingUnitValidator.validateNewTeachingUnit(dto);

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

        teachingUnitValidator.validateUpdatedTeachingUnit(oldCode,dto,existing);
        if (dto.code().equals(oldCode)) {
            existing.setLabel(dto.label());
            existing.setEcts(dto.ects());
            existing.setWorkloadHours(dto.workloadHours());
            existing.setObligation(dto.obligation());
            teachingUnitRepository.save(existing);
        }
        else {
            teachingUnitRefactorService.updateTeachingUnitCode(oldCode, dto);
        }
    }

    @Override
    public List<TeachingUnit> findAll() {
        return teachingUnitRepository.findAll();
    }

    @Override
    public Optional<TeachingUnit> findById(String id) {
        return teachingUnitRepository.findById(id);
    }

    @Transactional
    public void deleteById(String code) {
        teachingUnitRefactorService.deleteTeachingUnitWithReferences(code);
    }
}
