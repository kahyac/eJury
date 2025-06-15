package amu.jury_m1.service.impl;

import amu.jury_m1.dao.AnnualKnowledgeBlockRepository;
import amu.jury_m1.dao.CurriculumPlanRepository;
import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.service.api.AnnualKnowledgeBlockService;
import amu.jury_m1.service.dtos.AnnualKnowledgeBlockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnualKnowledgeBlockServiceImpl implements AnnualKnowledgeBlockService {

    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;
    private final CurriculumPlanRepository curriculumPlanRepository;

    @Transactional
    public void addAnnualKnowledgeBlock(AnnualKnowledgeBlockDto dto) {
        if (annualKnowledgeBlockRepository.existsByCode(dto.code())) {
            throw new IllegalArgumentException("Un bloc annuel avec cet identifiant existe déjà.");
        }

        CurriculumPlan plan = curriculumPlanRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("CurriculumPlan not found"));

        AnnualKnowledgeBlock block = AnnualKnowledgeBlock.builder()
                .code(dto.code())
                .curriculumPlan(plan)
                .build();

        plan.getAnnualKnowledgeBlocks().add(block);
        curriculumPlanRepository.save(plan);
    }

    @Transactional
    public void renameAnnualBlock(Long blockId, String newCode) {

        AnnualKnowledgeBlock block = annualKnowledgeBlockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc introuvable : " + blockId));


        if (annualKnowledgeBlockRepository.existsByCode(newCode)) {
            throw new IllegalArgumentException("Un bloc avec ce nom existe déjà.");
        }
        block.setCode(newCode);
        annualKnowledgeBlockRepository.save(block);
    }

    @Override
    public Optional<AnnualKnowledgeBlock> findById(Long id) {
        return annualKnowledgeBlockRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        annualKnowledgeBlockRepository.deleteById(id);
    }

    @Override
    public void save(AnnualKnowledgeBlock annualKnowledgeBlock) {
        annualKnowledgeBlockRepository.save(annualKnowledgeBlock);
    }
}
