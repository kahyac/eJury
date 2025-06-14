package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.SemestrialKnowledgeBlock;
import amu.jury_m1.service.dtos.SemestrialKnowledgeBlockDto;

public interface SemestrialKnowledgeBlockService {
    SemestrialKnowledgeBlock findById(Long id);
    void addSemestrialBlockToAnnual(String annualBlockId, SemestrialKnowledgeBlockDto dto);
    void associateTeachingUnitToSemestrialBlock(Long blockCode, String unitCode, double coefficient);
    String findAnnualIdBySemBlockId(Long blockId);
    void updateSemestrialBlock(Long blockId, SemestrialKnowledgeBlockDto dto);
    void deleteById(Long id);
}
