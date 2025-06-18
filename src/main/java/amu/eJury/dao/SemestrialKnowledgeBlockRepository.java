package amu.eJury.dao;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemestrialKnowledgeBlockRepository extends JpaRepository<SemestrialKnowledgeBlock, Long> {}
