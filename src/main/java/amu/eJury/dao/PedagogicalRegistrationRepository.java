package amu.eJury.dao;


import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedagogicalRegistrationRepository extends JpaRepository<PedagogicalRegistration, Long> {
    Optional<PedagogicalRegistration> findByStudentAndTeachingUnit(Student s, TeachingUnit u);
    List<PedagogicalRegistration> findByStudentId(Long studentId);
}