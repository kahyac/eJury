package amu.jury_m1.dao;

import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.result.AnnualBlockResult;
import amu.jury_m1.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnualBlockResultRepository extends JpaRepository<AnnualBlockResult, Long> {
    Optional<AnnualBlockResult> findByStudentAndAnnualBlock(Student student,
                                                            AnnualKnowledgeBlock annualBlock);
}
