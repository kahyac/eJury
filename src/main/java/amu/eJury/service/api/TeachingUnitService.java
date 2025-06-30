package amu.eJury.service.api;

import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.service.dtos.TeachingUnitDTO;

import java.util.List;
import java.util.Optional;

public interface TeachingUnitService {
    void addTeachingUnit(TeachingUnitDTO dto);
    void updateTeachingUnit(Long id, TeachingUnitDTO dto);
    List<TeachingUnit> findAll();
    Optional<TeachingUnit> findById(Long id);
    void deleteById(Long id);
}
