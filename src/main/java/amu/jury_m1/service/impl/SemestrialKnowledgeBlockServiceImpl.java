package amu.jury_m1.service.impl;

import amu.jury_m1.dao.AnnualKnowledgeBlockRepository;
import amu.jury_m1.dao.SemestrialKnowledgeBlockRepository;
import amu.jury_m1.dao.TeachingUnitRepository;
import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.pedagogy.SemestrialKnowledgeBlock;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.service.api.SemestrialKnowledgeBlockService;
import amu.jury_m1.service.dtos.SemestrialKnowledgeBlockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class git pSemestrialKnowledgeBlockServiceImpl implements SemestrialKnowledgeBlockService {

    private final SemestrialKnowledgeBlockRepository semestrialKnowledgeBlockRepository;
    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;
    private final TeachingUnitRepository teachingUnitRepository;

    @Transactional
    public void addSemestrialBlockToAnnual(String annualBlockId, SemestrialKnowledgeBlockDto dto) {
        AnnualKnowledgeBlock annual = annualKnowledgeBlockRepository.findById(annualBlockId)
                .orElseThrow(() -> new IllegalArgumentException("Annual block not found: " + annualBlockId));

        SemestrialKnowledgeBlock block = SemestrialKnowledgeBlock.builder()
                .code(dto.code())
                .label(dto.label())
                .semester(dto.semester())
                .ects(dto.ects())
                .annualKnowledgeBlock(annual)
                .build();

        semestrialKnowledgeBlockRepository.save(block);

        annual.getSemesters().add(block);
        annualKnowledgeBlockRepository.save(annual);
    }

    @Transactional
    public void associateTeachingUnitToSemestrialBlock(String blockCode, String unitCode, double coefficient) {
        TeachingUnit unit = teachingUnitRepository.findById(unitCode)
                .orElseThrow(() -> new IllegalArgumentException("TeachingUnit not found: " + unitCode));

        SemestrialKnowledgeBlock block = semestrialKnowledgeBlockRepository.findById(blockCode)
                .orElseThrow(() -> new IllegalArgumentException("Semestrial block not found: " + blockCode));

        block.getUnitsCoefficientAssociation().put(unit, coefficient);
        semestrialKnowledgeBlockRepository.save(block);
    }

    public String findAnnualIdBySemBlockCode(String blockCode) {
        return semestrialKnowledgeBlockRepository.findById(blockCode)
                .map(sb -> sb.getAnnualKnowledgeBlock().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bloc semestriel introuvable : " + blockCode));
    }
}
