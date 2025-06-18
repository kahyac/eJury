package amu.eJury.dao;

import amu.eJury.model.result.AcademicYearResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearResultRepository extends JpaRepository<AcademicYearResult, Long> {
}
