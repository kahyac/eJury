package amu.eJury.dao;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.SemestrialBlockResult;
import amu.eJury.model.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemestrialBlockResultRepository extends JpaRepository<SemestrialBlockResult, Long> {
    Optional<SemestrialBlockResult> findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(
            Student student, int semester, AnnualKnowledgeBlock annualKnowledgeBlock);
    List<SemestrialBlockResult> findByStudent(Student student);
    Optional<SemestrialBlockResult> findByStudentAndSemBlock(Student student, SemestrialKnowledgeBlock block);


}
