package amu.jury_m1.dao;

import amu.jury_m1.domain.result.KnowledgeBlockAnnualResult;
import amu.jury_m1.domain.student.StudentId;

import java.time.Year;
import java.util.List;

public interface ResultRepository {
    List<KnowledgeBlockAnnualResult> findByStudentAndYear(StudentId id, Year year);
    void saveAll(List<KnowledgeBlockAnnualResult> results);
}