package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.model.result.AcademicYearResult;
import amu.jury_m1.model.student.Student;

public interface YearDecisionCalculator {
    AcademicYearResult compute(Student s, CurriculumPlan plan);
}
