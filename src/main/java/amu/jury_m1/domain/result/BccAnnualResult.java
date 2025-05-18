package amu.jury_m1.domain.result;

import java.time.Year;

public record BccAnnualResult(
        String bccCode,
        Year academicYear,
        double averageS1,
        double averageS2,
        double annualAverage,
        Decision decision) {

    public enum Decision { ADMIS, ADM_COMP, AJOURNE }

    public static BccAnnualResult of(String code, Year y,
                                     double avgS1, double coefS1,
                                     double avgS2, double coefS2) {
        double annual = (avgS1 * coefS1 + avgS2 * coefS2) / (coefS1 + coefS2);

        Decision d;
        if (annual >= 10) d = Decision.ADMIS;
        else if (annual >= 9.5) d = Decision.ADM_COMP;
        else d = Decision.AJOURNE;

        return new BccAnnualResult(code, y, avgS1, avgS2, annual, d);
    }
}

