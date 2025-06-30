package amu.eJury.service.impl;

import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.service.api.TeachingUnitService;
import amu.eJury.service.dtos.TeachingUnitDTO;
import amu.eJury.service.validator.TeachingUnitValidator;
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

    @Transactional
    public void addTeachingUnit(TeachingUnitDTO dto) {
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
    public void updateTeachingUnit(Long id, TeachingUnitDTO dto) {
        TeachingUnit existing = teachingUnitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UE introuvable : " + id));

        teachingUnitValidator.validateUpdatedTeachingUnit(dto,existing);
        existing.setCode(dto.code());
        existing.setLabel(dto.label());
        existing.setEcts(dto.ects());
        existing.setWorkloadHours(dto.workloadHours());
        existing.setObligation(dto.obligation());
        teachingUnitRepository.save(existing);
    }

    public List<TeachingUnit> findAll() {
        return teachingUnitRepository.findAll();
    }

    public Optional<TeachingUnit> findById(Long id) {
        return teachingUnitRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        teachingUnitRepository.deleteById(id);
    }
}
