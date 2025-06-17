package amu.jury_m1.dao;

import amu.jury_m1.model.result.AcademicYearResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearResultRepository extends JpaRepository<AcademicYearResult, Long> {
}
