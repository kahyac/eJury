package amu.jury_m1.dao;

import amu.jury_m1.model.result.KnowledgeBlockAnnualResult;

import java.time.Year;
import java.util.List;

public interface ResultRepository {
    List<KnowledgeBlockAnnualResult> findByStudentAndYear(String id, Year year);
    void saveAll(List<KnowledgeBlockAnnualResult> results);
}