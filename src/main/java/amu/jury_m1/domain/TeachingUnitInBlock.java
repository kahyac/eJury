package amu.jury_m1.domain;

public class TeachingUnitInBlock {
    private final String teachingUnitCode;
    private final String blockCode;
    private final double coefficient;

    public TeachingUnitInBlock(String teachingUnitCode, String blockCode, double coefficient) {
        this.teachingUnitCode = teachingUnitCode;
        this.blockCode = blockCode;
        this.coefficient = coefficient;
    }

    public String getTeachingUnitCode() { return teachingUnitCode; }
    public String getBlockCode() { return blockCode; }
    public double getCoefficient() { return coefficient; }
}