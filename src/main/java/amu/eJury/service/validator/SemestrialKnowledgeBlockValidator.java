package amu.eJury.service.validator;

import amu.eJury.dao.AnnualKnowledgeBlockRepository;
import amu.eJury.dao.SemestrialKnowledgeBlockRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.pedagogy.TeachingUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SemestrialKnowledgeBlockValidator {

    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;
    private final SemestrialKnowledgeBlockRepository semestrialKnowledgeBlockRepository;
    private final TeachingUnitRepository teachingUnitRepository;

    public AnnualKnowledgeBlock validateAnnualBlockExists(Long annualId) {
        return annualKnowledgeBlockRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Annual block not found: " + annualId));
    }

    public SemestrialKnowledgeBlock validateSemestrialBlockExists(Long blockId) {
        return semestrialKnowledgeBlockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Semestrial block not found: " + blockId));
    }

    public TeachingUnit validateTeachingUnitExists(Long id) {
        return teachingUnitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TeachingUnit not found: " + id));
    }
}
