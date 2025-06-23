package amu.eJury.service.adapter;

import amu.eJury.dao.SemestrialBlockResultRepository;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialBlockResult;
import amu.eJury.model.users.Student;
import amu.eJury.service.port.SaveSemestrialResultPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class SemestrialResultJpaAdapter implements SaveSemestrialResultPort {

    private final SemestrialBlockResultRepository semestrialBlockResultRepository;

    @Override
    public SemestrialBlockResult save(SemestrialBlockResult result) {
        return semestrialBlockResultRepository.save(result);
    }

    @Override
    public Optional<SemestrialBlockResult> findByStudentAndBlock(Student s, SemestrialKnowledgeBlock block) {
        return semestrialBlockResultRepository.findByStudentAndSemBlock(s, block);
    }
}