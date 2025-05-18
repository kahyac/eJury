package amu.jury_m1.domain.rule;

import amu.jury_m1.domain.result.BccAnnualResult;
import java.time.Year;

public class AnnualRuleM3C implements AnnualCalculationStrategy {

    @Override
    public BccAnnualResult evaluate(String bccCode, Year year,
                                    double avgS1, double coefS1,
                                    double avgS2, double coefS2) {
        return BccAnnualResult.of(bccCode, year, avgS1, coefS1, avgS2, coefS2);
    }
}