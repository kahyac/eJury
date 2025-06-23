package amu.eJury.service.port;

import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;

import java.util.List;
import java.util.Optional;

public interface GradePort {
    Optional<TeachingUnitGrade> gradeOf(Student s, TeachingUnit u);
    List<PedagogicalRegistration> registrationsOf(Student student);

}
