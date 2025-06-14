package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.service.dtos.TeachingUnitDto;

import java.util.List;
import java.util.Optional;

public interface TeachingUnitService {
    void addTeachingUnit(TeachingUnitDto dto);
    void updateTeachingUnit(String oldCode, TeachingUnitDto dto);
    List<TeachingUnit> findAll();
    Optional<TeachingUnit> findById(String id);
    void deleteById(String id);
}
