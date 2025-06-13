package amu.jury_m1.service.api;

import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.service.dtos.AnnualKnowledgeBlockDto;

import java.util.Optional;

public interface AnnualKnowledgeBlockService {
    void addAnnualKnowledgeBlock(AnnualKnowledgeBlockDto dto);
    void renameAnnualBlock(String oldId, String newId);
    Optional<AnnualKnowledgeBlock> findById(String id);
    void deleteById(String id);
    void save(AnnualKnowledgeBlock annualKnowledgeBlock);
    void updateAnnualBlockId(String oldId, String newId);

}
