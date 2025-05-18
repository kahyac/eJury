package amu.jury_m1.domain.student;


import amu.jury_m1.domain.result.BccAnnualResult;
import amu.jury_m1.domain.rule.AnnualCalculationStrategy;

import java.time.Year;
import java.util.*;

public class Student {

    private final StudentId id;
    private final String lastName;
    private final String firstName;
    private final String email;

    private boolean archived;

    private final Map<String, BccAnnualResult> annualResults = new HashMap<>();

    public Student(StudentId id, String lastName, String firstName, String email) {
        this.id = Objects.requireNonNull(id);
        this.lastName = Objects.requireNonNull(lastName);
        this.firstName = Objects.requireNonNull(firstName);
        this.email = Objects.requireNonNull(email);
        this.archived = false;
    }

    public StudentId id() { return id; }
    public String lastName() { return lastName; }
    public String firstName() { return firstName; }
    public String email() { return email; }
    public boolean isArchived() { return archived; }

    public void archive() {
        this.archived = true;
    }

    public BccAnnualResult computeAnnualBcc(String bccCode, Year year,
                                            double avgS1, double coefS1,
                                            double avgS2, double coefS2) {
        var result = BccAnnualResult.of(bccCode, year, avgS1, coefS1, avgS2, coefS2);
        annualResults.put(bccCode, result);
        return result;
    }

    public List<BccAnnualResult> computeAllResults(Year year,
                                                   Map<String, double[]> bccInputs,
                                                   AnnualCalculationStrategy strategy) {
        List<BccAnnualResult> results = new ArrayList<>();
        for (var entry : bccInputs.entrySet()) {
            var bccCode = entry.getKey();
            var scores = entry.getValue(); // [avgS1, coefS1, avgS2, coefS2]
            var result = strategy.evaluate(bccCode, year, scores[0], scores[1], scores[2], scores[3]);
            annualResults.put(bccCode, result);
            results.add(result);
        }
        return results;
    }

    public Optional<BccAnnualResult> resultFor(String bccCode) {
        return Optional.ofNullable(annualResults.get(bccCode));
    }
}
