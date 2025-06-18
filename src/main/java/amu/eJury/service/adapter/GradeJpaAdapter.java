package amu.eJury.service.adapter;

import amu.eJury.dao.TeachingUnitGradeRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.student.Student;
import amu.eJury.service.port.GradePort;
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