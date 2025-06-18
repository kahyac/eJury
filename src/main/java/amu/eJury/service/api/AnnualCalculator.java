package amu.eJury.service.api;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.result.AnnualBlockResult;
import amu.eJury.model.student.Student;

public interface AnnualCalculator {
    AnnualBlockResult compute(Student s, AnnualKnowledgeBlock block);
}