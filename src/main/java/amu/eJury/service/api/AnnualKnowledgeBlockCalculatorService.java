package amu.eJury.service.api;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.result.AnnualKnowledgeBlockResult;
import amu.eJury.model.users.Student;

public interface AnnualKnowledgeBlockCalculatorService {
    AnnualKnowledgeBlockResult compute(Student s, AnnualKnowledgeBlock block);
}