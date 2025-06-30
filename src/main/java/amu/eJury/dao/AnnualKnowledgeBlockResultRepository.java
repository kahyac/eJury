package amu.eJury.dao;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.result.AnnualKnowledgeBlockResult;
import amu.eJury.model.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualKnowledgeBlockResultRepository extends JpaRepository<AnnualKnowledgeBlockResult, Long> {
    Optional<AnnualKnowledgeBlockResult> findByStudentAndAnnualBlock(Student student,
                                                                     AnnualKnowledgeBlock annualBlock);
    List<AnnualKnowledgeBlockResult> findByStudent(Student student);
}
