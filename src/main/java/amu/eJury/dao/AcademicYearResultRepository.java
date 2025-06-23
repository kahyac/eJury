package amu.eJury.dao;

import amu.eJury.model.result.AcademicYearResult;
import amu.eJury.model.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicYearResultRepository extends JpaRepository<AcademicYearResult, Long> {
    Optional<AcademicYearResult> findByStudent(Student student);
}
