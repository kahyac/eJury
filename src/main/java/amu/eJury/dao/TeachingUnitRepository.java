package amu.eJury.dao;

import amu.eJury.model.pedagogy.TeachingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeachingUnitRepository extends JpaRepository<TeachingUnit, Long> {
    boolean existsByCode(String code);
    boolean existsByLabel(String label);
    List<TeachingUnit> findByObligationFalse();
    List<TeachingUnit> findByObligationTrue();
    Optional<TeachingUnit> findByResponsibleTeacherId(Long id);;
}
