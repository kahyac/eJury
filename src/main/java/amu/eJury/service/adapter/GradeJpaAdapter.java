package amu.eJury.service.adapter;

import amu.eJury.dao.PedagogicalRegistrationRepository;
import amu.eJury.dao.StudentRepository;
import amu.eJury.dao.TeachingUnitGradeRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import amu.eJury.service.port.GradePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class GradeJpaAdapter implements GradePort {
    private final PedagogicalRegistrationRepository registrationRepository;

    @Override
    public List<PedagogicalRegistration> registrationsOf(Student student) {
        return registrationRepository.findByStudent(student);
    }
}