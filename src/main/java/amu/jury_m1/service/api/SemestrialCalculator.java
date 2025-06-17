package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.SemestrialKnowledgeBlock;
import amu.jury_m1.model.result.SemestrialBlockResult;
import amu.jury_m1.model.student.Student;

public interface SemestrialCalculator {
    SemestrialBlockResult compute(Student s, SemestrialKnowledgeBlock block);
}