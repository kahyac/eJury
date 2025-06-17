package amu.jury_m1.service.adapter;

import amu.jury_m1.dao.TeachingUnitGradeRepository;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.model.result.TeachingUnitGrade;
import amu.jury_m1.model.student.Student;
import amu.jury_m1.service.port.GradePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class GradeJpaAdapter implements GradePort {
    private final TeachingUnitGradeRepository teachingUnitGradeRepository;
    public Optional<TeachingUnitGrade> gradeOf(Student s, TeachingUnit u){
        return teachingUnitGradeRepository.findByRegistration_StudentAndRegistration_TeachingUnit(s, u);
    }
}