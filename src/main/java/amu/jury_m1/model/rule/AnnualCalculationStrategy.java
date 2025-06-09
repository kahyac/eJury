package amu.jury_m1.domain.rule;

import amu.jury_m1.domain.result.KnowledgeBlockAnnualResult;
import amu.jury_m1.domain.student.StudentId;

import java.time.Year;

public interface AnnualCalculationStrategy {
    KnowledgeBlockAnnualResult evaluate(StudentId studentId, String bccCode, Year year,
                                        double avgS1, double coefS1,
                                        double avgS2, double coefS2);
}