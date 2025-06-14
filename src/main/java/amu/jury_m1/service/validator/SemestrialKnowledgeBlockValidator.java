package amu.jury_m1.service.validator;

import amu.jury_m1.dao.AnnualKnowledgeBlockRepository;
import amu.jury_m1.dao.SemestrialKnowledgeBlockRepository;
import amu.jury_m1.dao.TeachingUnitRepository;
import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.pedagogy.SemestrialKnowledgeBlock;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SemestrialKnowledgeBlockValidator {

    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;
    private final SemestrialKnowledgeBlockRepository semestrialKnowledgeBlockRepository;
    private final TeachingUnitRepository teachingUnitRepository;

    public AnnualKnowledgeBlock validateAnnualBlockExists(String annualId) {
        return annualKnowledgeBlockRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Annual block not found: " + annualId));
    }

    public SemestrialKnowledgeBlock validateSemestrialBlockExists(Long blockId) {
        return semestrialKnowledgeBlockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Semestrial block not found: " + blockId));
    }

    public TeachingUnit validateTeachingUnitExists(String unitCode) {
        return teachingUnitRepository.findById(unitCode)
                .orElseThrow(() -> new IllegalArgumentException("TeachingUnit not found: " + unitCode));
    }
}
