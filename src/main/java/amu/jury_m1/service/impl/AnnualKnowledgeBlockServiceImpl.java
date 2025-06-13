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
        if (annualKnowledgeBlockRepository.existsById(dto.id())) {
            throw new IllegalArgumentException("Un bloc annuel avec cet identifiant existe déjà.");
        }

        AnnualKnowledgeBlock block = AnnualKnowledgeBlock.builder()
                .id(dto.id())
                .build();

        CurriculumPlan plan = curriculumPlanRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("CurriculumPlan not found"));

        plan.getAnnualKnowledgeBlocks().add(block);
        curriculumPlanRepository.save(plan);
    }

    @Transactional
    public void renameAnnualBlock(String oldId, String newId) {

        AnnualKnowledgeBlock block = annualKnowledgeBlockRepository.findById(oldId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc introuvable : " + oldId));


        if (annualKnowledgeBlockRepository.existsById(newId)) {
            throw new IllegalArgumentException("Un bloc avec ce nom existe déjà.");
        }

        AnnualKnowledgeBlock renamed = AnnualKnowledgeBlock.builder()
                .id(newId)
                .curriculumPlan(block.getCurriculumPlan())
                .semesters(block.getSemesters())
                .build();

        CurriculumPlan plan = block.getCurriculumPlan();
        plan.getAnnualKnowledgeBlocks().removeIf(b -> b.getId().equals(oldId));
        plan.getAnnualKnowledgeBlocks().add(renamed);

        curriculumPlanRepository.save(plan);
    }

    @Override
    public Optional<AnnualKnowledgeBlock> findById(String id) {
        return annualKnowledgeBlockRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        annualKnowledgeBlockRepository.deleteById(id);
    }

    @Override
    public void save(AnnualKnowledgeBlock annualKnowledgeBlock) {
        annualKnowledgeBlockRepository.save(annualKnowledgeBlock);
    }

    @Override
    @Transactional
    public void updateAnnualBlockId(String oldId, String newId) {
        AnnualKnowledgeBlock block = annualKnowledgeBlockRepository.findById(oldId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc non trouvé : " + oldId));

        if (!block.getId().equals(newId)) {
            annualKnowledgeBlockRepository.deleteById(oldId);
            block.setId(newId);
            annualKnowledgeBlockRepository.save(block);
        }
    }
}
