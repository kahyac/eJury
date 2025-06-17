package amu.jury_m1.dao;

import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.model.registration.PedagogicalRegistration;
import amu.jury_m1.model.result.TeachingUnitGrade;
import amu.jury_m1.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachingUnitGradeRepository extends JpaRepository<TeachingUnitGrade, Long> {
    Optional<TeachingUnitGrade> findByRegistration(PedagogicalRegistration reg);
    Optional<TeachingUnitGrade> findByRegistration_StudentAndRegistration_TeachingUnit(
            Student student, TeachingUnit teachingUnit);
}
