package amu.eJury.dao;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.result.AnnualBlockResult;
import amu.eJury.model.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualBlockResultRepository extends JpaRepository<AnnualBlockResult, Long> {
    Optional<AnnualBlockResult> findByStudentAndAnnualBlock(Student student,
                                                            AnnualKnowledgeBlock annualBlock);
    List<AnnualBlockResult> findByStudent(Student student);
}
