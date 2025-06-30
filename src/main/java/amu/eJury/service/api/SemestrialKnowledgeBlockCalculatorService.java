package amu.eJury.service.api;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialKnowledgeBlockResult;
import amu.eJury.model.users.Student;

public interface SemestrialKnowledgeBlockCalculatorService {
    SemestrialKnowledgeBlockResult compute(Student s, SemestrialKnowledgeBlock block);
}