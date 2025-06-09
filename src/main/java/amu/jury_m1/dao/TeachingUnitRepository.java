package amu.jury_m1.dao;

import amu.jury_m1.model.pedagogy.TeachingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingUnitRepository extends JpaRepository<TeachingUnit, String> {}
