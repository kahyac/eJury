package amu.jury_m1.service.impl;

import amu.jury_m1.dao.TeachingUnitRepository;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.service.api.TeachingUnitService;
import amu.jury_m1.service.dtos.TeachingUnitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeachingUnitServiceImpl implements TeachingUnitService {

    private final TeachingUnitRepository teachingUnitRepository;

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

    @Override
    public List<TeachingUnit> findAll() {
        return teachingUnitRepository.findAll();
    }

    @Override
    public Optional<TeachingUnit> findById(String id) {
        return teachingUnitRepository.findById(id);
    }
}
