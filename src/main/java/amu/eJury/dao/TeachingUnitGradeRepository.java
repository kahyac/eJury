package amu.eJury.dao;

import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachingUnitGradeRepository extends JpaRepository<TeachingUnitGrade, Long> {
    Optional<TeachingUnitGrade> findByRegistration(PedagogicalRegistration reg);
    Optional<TeachingUnitGrade> findByRegistration_StudentAndRegistration_TeachingUnit(
            Student student, TeachingUnit teachingUnit);
}
