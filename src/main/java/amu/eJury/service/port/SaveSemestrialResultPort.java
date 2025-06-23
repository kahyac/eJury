package amu.eJury.service.port;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialBlockResult;
import amu.eJury.model.users.Student;
import org.springframework.cglib.core.Block;

import java.util.Optional;

public interface SaveSemestrialResultPort {
    SemestrialBlockResult save(SemestrialBlockResult r);
    Optional<SemestrialBlockResult> findByStudentAndBlock(Student s, SemestrialKnowledgeBlock block);

}
