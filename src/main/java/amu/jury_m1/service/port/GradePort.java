package amu.jury_m1.service.port;

import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.model.result.TeachingUnitGrade;
import amu.jury_m1.model.student.Student;

import java.util.Optional;

public interface GradePort {
    Optional<TeachingUnitGrade> gradeOf(Student s, TeachingUnit u);
}
