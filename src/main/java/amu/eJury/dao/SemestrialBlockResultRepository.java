package amu.eJury.dao;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.result.SemestrialBlockResult;
import amu.eJury.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemestrialBlockResultRepository extends JpaRepository<SemestrialBlockResult, Long> {
    Optional<SemestrialBlockResult> findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(
            Student student, int semester, AnnualKnowledgeBlock annualKnowledgeBlock);

}
