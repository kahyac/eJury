package amu.jury_m1.model.result;

import amu.jury_m1.model.student.StudentId;

import java.time.Year;
import java.util.Objects;

public class KnowledgeBlockAnnualResult {

    private final StudentId studentId;
    private final String blockCode;
    private final Year academicYear;

    private final double averageS1;
    private final double averageS2;
    private final double annualAverage;

    private final AnnualDecision decision;
    private final Mention mention;

    public KnowledgeBlockAnnualResult(StudentId studentId,
                                      String blockCode,
                                      Year academicYear,
                                      double averageS1,
                                      double averageS2,
                                      double annualAverage,
                                      AnnualDecision decision,
                                      Mention mention) {
        this.studentId = Objects.requireNonNull(studentId);
        this.blockCode = Objects.requireNonNull(blockCode);
        this.academicYear = Objects.requireNonNull(academicYear);
        this.averageS1 = averageS1;
        this.averageS2 = averageS2;
        this.annualAverage = annualAverage;
        this.decision = Objects.requireNonNull(decision);
        this.mention = Objects.requireNonNull(mention);
    }

    // --- Fabrique statique : création d'un résultat à partir des notes ---
    public static KnowledgeBlockAnnualResult of(StudentId studentId,
                                                String blockCode,
                                                Year year,
                                                double avgS1,
                                                double coefS1,
                                                double avgS2,
                                                double coefS2) {

        double annual = (avgS1 * coefS1 + avgS2 * coefS2) / (coefS1 + coefS2);

        // Décision selon règle M3C
        AnnualDecision decision;
        if (annual >= 10) {
            decision = AnnualDecision.PASSED;
        }else {
            decision = AnnualDecision.FAILED;
        }

        // Mention calculée selon moyenne
        Mention mention = computeMention(annual);

        return new KnowledgeBlockAnnualResult(studentId, blockCode, year, avgS1, avgS2, annual, decision, mention);
    }

    // --- Attribution de la mention en fonction de la moyenne ---
    private static Mention computeMention(double average) {
        if (average >= 16) return Mention.EXCELLENT;
        else if (average >= 14) return Mention.VERY_GOOD;
        else if (average >= 12) return Mention.GOOD;
        else if (average >= 10) return Mention.FAIRLY_GOOD;
        else if (average >= 9.5) return Mention.PASS;
        else return Mention.NONE;
    }

    // --- Getters ---

    public StudentId getStudentId() { return studentId; }
    public String getBlockCode() { return blockCode; }
    public Year getAcademicYear() { return academicYear; }
    public double getAverageS1() { return averageS1; }
    public double getAverageS2() { return averageS2; }
    public double getAnnualAverage() { return annualAverage; }
    public AnnualDecision getDecision() { return decision; }
    public Mention getMention() { return mention; }
}