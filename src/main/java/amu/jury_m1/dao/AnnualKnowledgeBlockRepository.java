package amu.jury_m1.dao;

import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnualKnowledgeBlockRepository extends JpaRepository<AnnualKnowledgeBlock, Long> {
    boolean existsByCode(String code);
    AnnualKnowledgeBlock findByCode(String code);
}
