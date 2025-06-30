package amu.eJury.service.adapter;

import amu.eJury.dao.SemestrialKnowledgeBlockResultRepository;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialKnowledgeBlockResult;
import amu.eJury.model.users.Student;
import amu.eJury.service.port.SaveSemestrialResultPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class SemestrialResultJpaAdapter implements SaveSemestrialResultPort {

    private final SemestrialKnowledgeBlockResultRepository semestrialKnowledgeBlockResultRepository;

    @Override
    public SemestrialKnowledgeBlockResult save(SemestrialKnowledgeBlockResult result) {
        return semestrialKnowledgeBlockResultRepository.save(result);
    }

    @Override
    public Optional<SemestrialKnowledgeBlockResult> findByStudentAndBlock(Student s, SemestrialKnowledgeBlock block) {
        return semestrialKnowledgeBlockResultRepository.findByStudentAndSemBlock(s, block);
    }
}