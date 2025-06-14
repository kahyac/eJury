package amu.jury_m1.service.refactor;

import amu.jury_m1.dao.SemestrialKnowledgeBlockRepository;
import amu.jury_m1.dao.TeachingUnitRepository;
import amu.jury_m1.model.pedagogy.SemestrialKnowledgeBlock;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import amu.jury_m1.service.dtos.TeachingUnitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TeachingUnitRefactorService {

    private final TeachingUnitRepository teachingUnitRepository;
    private final SemestrialKnowledgeBlockRepository semestrialBlockRepository;

    @Transactional
    public void updateTeachingUnitCode(String oldCode, TeachingUnitDto newDto) {
        TeachingUnit existing = teachingUnitRepository.findById(oldCode)
                .orElseThrow(() -> new IllegalArgumentException("UE introuvable : " + oldCode));

        List<SemestrialKnowledgeBlock> allBlocks = semestrialBlockRepository.findAll();
        Map<SemestrialKnowledgeBlock, Double> associatedCoefficients = new HashMap<>();

        for (SemestrialKnowledgeBlock block : allBlocks) {
            Double coef = block.getUnitsCoefficientAssociation().remove(existing);
            if (coef != null) {
                associatedCoefficients.put(block, coef);
            }
        }

        teachingUnitRepository.delete(existing);

        TeachingUnit updated = TeachingUnit.builder()
                .code(newDto.code())
                .label(newDto.label())
                .ects(newDto.ects())
                .workloadHours(newDto.workloadHours())
                .obligation(newDto.obligation())
                .build();

        teachingUnitRepository.save(updated);

        for (Map.Entry<SemestrialKnowledgeBlock, Double> entry : associatedCoefficients.entrySet()) {
            SemestrialKnowledgeBlock block = entry.getKey();
            block.getUnitsCoefficientAssociation().put(updated, entry.getValue());
            semestrialBlockRepository.save(block);
        }
    }

    @Transactional
    public void deleteTeachingUnitWithReferences(String code) {
        TeachingUnit unit = teachingUnitRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("UE introuvable : " + code));

        List<SemestrialKnowledgeBlock> allBlocks = semestrialBlockRepository.findAll();

        for (SemestrialKnowledgeBlock block : allBlocks) {
            if (block.getUnitsCoefficientAssociation().containsKey(unit)) {
                block.getUnitsCoefficientAssociation().remove(unit);
                semestrialBlockRepository.save(block);
            }
        }

        teachingUnitRepository.delete(unit);
    }
}
