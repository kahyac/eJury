package amu.eJury.service.api;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.service.dtos.AnnualKnowledgeBlockDTO;

import java.util.Optional;

public interface AnnualKnowledgeBlockService {
    void addAnnualKnowledgeBlock(AnnualKnowledgeBlockDTO dto);
    void renameAnnualBlock(Long blockId, String newCode);
    Optional<AnnualKnowledgeBlock> findById(Long id);
    void deleteById(Long id);
    void save(AnnualKnowledgeBlock annualKnowledgeBlock);

}
