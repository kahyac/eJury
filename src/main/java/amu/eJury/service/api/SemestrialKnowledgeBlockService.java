package amu.eJury.service.api;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.service.dtos.SemestrialKnowledgeBlockDto;

public interface SemestrialKnowledgeBlockService {
    SemestrialKnowledgeBlock findById(Long id);
    void addSemestrialBlockToAnnual(Long annualBlockId, SemestrialKnowledgeBlockDto dto);
    void associateTeachingUnitToSemestrialBlock(Long blockId, Long unitId, double coefficient);
    Long findAnnualIdBySemBlockId(Long blockId);
    void updateSemestrialBlock(Long blockId, SemestrialKnowledgeBlockDto dto);
    void deleteById(Long id);
    void removeUnitFromSemestrialBlock(Long semestrialBlockId, Long unitId);
}
