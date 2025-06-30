package amu.eJury.dao;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialKnowledgeBlockResult;
import amu.eJury.model.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemestrialKnowledgeBlockResultRepository extends JpaRepository<SemestrialKnowledgeBlockResult, Long> {
    Optional<SemestrialKnowledgeBlockResult> findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(
            Student student, int semester, AnnualKnowledgeBlock annualKnowledgeBlock);
    List<SemestrialKnowledgeBlockResult> findByStudent(Student student);
    Optional<SemestrialKnowledgeBlockResult> findByStudentAndSemBlock(Student student, SemestrialKnowledgeBlock block);


}

