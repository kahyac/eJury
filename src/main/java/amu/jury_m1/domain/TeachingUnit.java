package amu.jury_m1.domain;

public class TeachingUnit {
    private final String code;
    private final String label;
    private final double ects;
    private final double workloadHours;
    private final double coefficient;

    public TeachingUnit(String code, String label, double ects, double workloadHours, double coefficient) {
        this.code = code;
        this.label = label;
        this.ects = ects;
        this.workloadHours = workloadHours;
        this.coefficient = coefficient;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }
    public double getEcts() { return ects; }
    public double getWorkloadHours() { return workloadHours; }
    public double getCoefficient() { return coefficient; }
}
