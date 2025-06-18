package amu.eJury.dao;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnnualKnowledgeBlockRepository extends JpaRepository<AnnualKnowledgeBlock, Long> {
    boolean existsByCode(String code);
    AnnualKnowledgeBlock findByCode(String code);
}
