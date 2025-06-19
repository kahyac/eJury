package amu.jury_m1.dao;


import amu.jury_m1.model.registration.PedagogicalRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedagogicalRegistrationRepository extends JpaRepository<PedagogicalRegistration, String> {
    List<PedagogicalRegistration> findByStudentId(String studentId);
    Optional<PedagogicalRegistration> findByStudentIdAndTeachingUnitId(String studentId, Long teachingUnitId);
}