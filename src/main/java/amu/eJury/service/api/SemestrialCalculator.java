package amu.eJury.service.api;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialBlockResult;
import amu.eJury.model.student.Student;

public interface SemestrialCalculator {
    SemestrialBlockResult compute(Student s, SemestrialKnowledgeBlock block);
}