package amu.eJury.service.impl;

import amu.eJury.dao.PedagogicalRegistrationRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.RegistrationService;
import org.springframework.stereotype.Service;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final TeachingUnitRepository teachingUnitRepository;
    private final PedagogicalRegistrationRepository registrationRepository;

    public void registerMandatoryUnitsForStudent(Student student) {
        List<TeachingUnit> mandatoryUnits = teachingUnitRepository.findByObligationTrue();

        for (TeachingUnit unit : mandatoryUnits) {
            PedagogicalRegistration registration = PedagogicalRegistration.builder()
                    .student(student)
                    .teachingUnit(unit)
                    .semester(unit.getSemester())
                    .build();

            registrationRepository.save(registration);
        }
    }

    public void deleteRegistration(Long studentId, Long unitId) {
        Optional<PedagogicalRegistration> reg = registrationRepository.findByStudentIdAndTeachingUnitId(studentId, unitId);
        reg.ifPresent(registrationRepository::delete);
    }

    @Override
    public Map<Student, Integer> checkEctsCompletenessForSemester(int semester) {
        List<PedagogicalRegistration> allRegistrations = registrationRepository.findAll();

        Map<Student, List<PedagogicalRegistration>> groupedByStudent = allRegistrations.stream()
                .filter(reg -> reg.getSemester() == semester)
                .collect(Collectors.groupingBy(PedagogicalRegistration::getStudent));

        Map<Student, Integer> missingCredits = new HashMap<>();

        for (Map.Entry<Student, List<PedagogicalRegistration>> entry : groupedByStudent.entrySet()) {
            Student student = entry.getKey();
            List<PedagogicalRegistration> regs = entry.getValue();

            int totalEcts = regs.stream()
                    .mapToInt(reg -> (int) reg.getTeachingUnit().getEcts())
                    .sum();

            if (totalEcts < 30) {
                missingCredits.put(student, 30 - totalEcts);
            }
        }

        return missingCredits;
    }



}
