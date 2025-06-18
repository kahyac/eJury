package amu.eJury.service.port;

import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.student.Student;

import java.util.Optional;

public interface GradePort {
    Optional<TeachingUnitGrade> gradeOf(Student s, TeachingUnit u);
}
