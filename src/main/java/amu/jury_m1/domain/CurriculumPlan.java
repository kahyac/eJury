package amu.jury_m1.domain;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurriculumPlan {
    private final Year academicYear;
    private final List<TeachingUnit> teachingUnits = new ArrayList<>();
    private final List<KnowledgeBlock> knowledgeBlocks = new ArrayList<>();
    private final List<TeachingUnitInBlock> associations = new ArrayList<>();

    public CurriculumPlan(Year academicYear) {
        this.academicYear = academicYear;
    }

    public Year getAcademicYear() { return academicYear; }
    public List<TeachingUnit> getTeachingUnits() { return teachingUnits; }
    public List<KnowledgeBlock> getKnowledgeBlocks() { return knowledgeBlocks; }
    public List<TeachingUnitInBlock> getAssociations() { return associations; }

    public void addTeachingUnit(TeachingUnit ue) {
        Objects.requireNonNull(ue);
        teachingUnits.add(ue);
    }

    public void addKnowledgeBlock(KnowledgeBlock bcc) {
        Objects.requireNonNull(bcc);
        knowledgeBlocks.add(bcc);
    }

    public void attachTeachingUnitToBlock(String ueCode, String bccCode, double coefficient) {
        associations.add(new TeachingUnitInBlock(ueCode, bccCode, coefficient));
    }
}
