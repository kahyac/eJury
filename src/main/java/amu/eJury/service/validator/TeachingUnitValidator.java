package amu.eJury.service.validator;

import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.service.dtos.TeachingUnitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeachingUnitValidator {

    private final TeachingUnitRepository teachingUnitRepository;

    public void validateNewTeachingUnit(TeachingUnitDto dto) {
        if (teachingUnitRepository.existsByCode(dto.code())) {
            throw new IllegalArgumentException("Une UE avec ce code existe déjà.");
        }

        if (teachingUnitRepository.existsByLabel(dto.label())) {
            throw new IllegalArgumentException("Une UE avec ce libellé existe déjà.");
        }

        validateDtoFields(dto);
    }

    public void validateUpdatedTeachingUnit(TeachingUnitDto dto, TeachingUnit existing) {
        boolean labelConflict = teachingUnitRepository.existsByLabel(dto.label())
                && !existing.getLabel().equals(dto.label()) ;
        if (labelConflict) {
            throw new IllegalArgumentException("Ce libellé est déjà utilisé.");
        }

        boolean codeConflict = teachingUnitRepository.existsByCode(dto.code()) && !existing.getCode().equals(dto.code());
        if (codeConflict) {
            throw new IllegalArgumentException("Ce code est déjà utilisé.");
        }

        validateDtoFields(dto);
    }

    private void validateDtoFields(TeachingUnitDto dto) {
        if (dto.ects() < 0 || dto.ects() > 30) {
            throw new IllegalArgumentException("Le nombre d’ECTS est invalide.");
        }
        if (dto.workloadHours() < 0 || dto.workloadHours() > 300) {
            throw new IllegalArgumentException("Le volume horaire est invalide.");
        }
    }
}
