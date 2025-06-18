package amu.eJury.service.api;

import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.model.result.AcademicYearResult;
import amu.eJury.model.student.Student;

public interface YearDecisionCalculator {
    AcademicYearResult compute(Student s, CurriculumPlan plan);
}
