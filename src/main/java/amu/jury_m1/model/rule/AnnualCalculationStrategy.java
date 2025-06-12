package amu.jury_m1.model.rule;

import amu.jury_m1.model.result.KnowledgeBlockAnnualResult;


import java.time.Year;

public interface AnnualCalculationStrategy {
    KnowledgeBlockAnnualResult evaluate(String studentId, String bccCode, Year year,
                                        double avgS1, double coefS1,
                                        double avgS2, double coefS2);
}