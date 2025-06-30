package amu.eJury.service.port;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialKnowledgeBlockResult;
import amu.eJury.model.users.Student;

import java.util.Optional;

public interface SaveSemestrialResultPort {
    SemestrialKnowledgeBlockResult save(SemestrialKnowledgeBlockResult r);
    Optional<SemestrialKnowledgeBlockResult> findByStudentAndBlock(Student s, SemestrialKnowledgeBlock block);

}
