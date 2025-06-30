package amu.eJury.service.impl;

import amu.eJury.dao.AnnualKnowledgeBlockRepository;
import amu.eJury.dao.SemestrialKnowledgeBlockRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.service.api.SemestrialKnowledgeBlockService;
import amu.eJury.service.dtos.SemestrialKnowledgeBlockDTO;
import amu.eJury.service.validator.SemestrialKnowledgeBlockValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SemestrialKnowledgeBlockServiceImpl implements SemestrialKnowledgeBlockService {

    private final SemestrialKnowledgeBlockRepository semestrialKnowledgeBlockRepository;
    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final SemestrialKnowledgeBlockValidator semestrialKnowledgeBlockValidator;

    @Transactional
    public void addSemestrialBlockToAnnual(Long annualBlockId, SemestrialKnowledgeBlockDTO dto) {
        AnnualKnowledgeBlock annual = semestrialKnowledgeBlockValidator.validateAnnualBlockExists(annualBlockId);

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
    public void associateTeachingUnitToSemestrialBlock(Long blockId, Long id, double coefficient) {
        SemestrialKnowledgeBlock block = semestrialKnowledgeBlockValidator.validateSemestrialBlockExists(blockId);
        TeachingUnit unit = semestrialKnowledgeBlockValidator.validateTeachingUnitExists(id);

        block.getUnitsCoefficientAssociation().put(unit, coefficient);
        semestrialKnowledgeBlockRepository.save(block);
    }

    public Long findAnnualIdBySemBlockId(Long blockId) {
        return semestrialKnowledgeBlockValidator.validateSemestrialBlockExists(blockId)
                .getAnnualKnowledgeBlock()
                .getId();
    }

    @Transactional
    @Override
    public void updateSemestrialBlock(Long blockId, SemestrialKnowledgeBlockDTO dto) {
        SemestrialKnowledgeBlock block = semestrialKnowledgeBlockValidator.validateSemestrialBlockExists(blockId);

        block.setLabel(dto.label());
        block.setSemester(dto.semester());
        block.setEcts(dto.ects());
        block.setCode(dto.code());

        semestrialKnowledgeBlockRepository.save(block);
    }


    public SemestrialKnowledgeBlock findById(Long id) {
        return semestrialKnowledgeBlockValidator.validateSemestrialBlockExists(id);
    }

    @Transactional
    public void deleteById(Long code) {
        semestrialKnowledgeBlockRepository.deleteById(code);
    }

    @Override
    public void removeUnitFromSemestrialBlock(Long semestrialBlockId, Long unitId) {
            SemestrialKnowledgeBlock semestrialBlock = semestrialKnowledgeBlockRepository.findById(semestrialBlockId)
                    .orElseThrow(() -> new IllegalArgumentException("Bloc semestriel non trouvé"));

            TeachingUnit unit = teachingUnitRepository.findById(unitId)
                    .orElseThrow(() -> new IllegalArgumentException("UE non trouvée"));

            semestrialBlock.getUnitsCoefficientAssociation().remove(unit);
            semestrialKnowledgeBlockRepository.save(semestrialBlock);
    }

}
