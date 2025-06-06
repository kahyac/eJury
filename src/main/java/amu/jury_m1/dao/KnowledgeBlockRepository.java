package amu.jury_m1.dao;

import amu.jury_m1.domain.pedagogy.KnowledgeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeBlockRepository extends JpaRepository<KnowledgeBlock, String> {}
