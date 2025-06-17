package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.result.AnnualBlockResult;
import amu.jury_m1.model.student.Student;

public interface AnnualCalculator {
    AnnualBlockResult compute(Student s, AnnualKnowledgeBlock block);
}