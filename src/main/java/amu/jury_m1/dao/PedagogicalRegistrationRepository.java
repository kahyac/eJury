package amu.jury_m1.dao;


import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.model.registration.PedagogicalRegistration;
import amu.jury_m1.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedagogicalRegistrationRepository extends JpaRepository<PedagogicalRegistration, String> {
    Optional<PedagogicalRegistration> findByStudentAndTeachingUnit(Student s, TeachingUnit u);
    List<PedagogicalRegistration> findByStudentId(String studentId);
}