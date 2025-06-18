package amu.eJury.service.api;

import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.service.dtos.TeachingUnitDto;

import java.util.List;
import java.util.Optional;

public interface TeachingUnitService {
    void addTeachingUnit(TeachingUnitDto dto);
    void updateTeachingUnit(Long id, TeachingUnitDto dto);
    List<TeachingUnit> findAll();
    Optional<TeachingUnit> findById(Long id);
    void deleteById(Long id);
}
