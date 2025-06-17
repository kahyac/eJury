package amu.jury_m1.dao;

import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.result.SemestrialBlockResult;
import amu.jury_m1.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemestrialBlockResultRepository extends JpaRepository<SemestrialBlockResult, Long> {
    Optional<SemestrialBlockResult> findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(
            Student student, int semester, AnnualKnowledgeBlock annualKnowledgeBlock);

}
