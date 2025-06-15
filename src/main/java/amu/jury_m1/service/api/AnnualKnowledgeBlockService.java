package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.service.dtos.AnnualKnowledgeBlockDto;

import java.util.Optional;

public interface AnnualKnowledgeBlockService {
    void addAnnualKnowledgeBlock(AnnualKnowledgeBlockDto dto);
    void renameAnnualBlock(Long blockId, String newCode);
    Optional<AnnualKnowledgeBlock> findById(Long id);
    void deleteById(Long id);
    void save(AnnualKnowledgeBlock annualKnowledgeBlock);

}
