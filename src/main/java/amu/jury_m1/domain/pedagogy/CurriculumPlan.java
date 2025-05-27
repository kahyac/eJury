package amu.jury_m1.domain.pedagogy;

import lombok.Getter;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CurriculumPlan — Represents a pedagogical plan (maquette) for a given academic year.
 */

@Getter
public class CurriculumPlan {
    private final Year academicYear;
    private final List<TeachingUnit> teachingUnits = new ArrayList<>();
    private final List<KnowledgeBlock> knowledgeBlocks = new ArrayList<>();
    private final List<UnitInBlockAssociation> associations = new ArrayList<>();

    public CurriculumPlan(Year academicYear) {
        this.academicYear = Objects.requireNonNull(academicYear);
    }

    public void addTeachingUnit(TeachingUnit unit) {
        teachingUnits.add(unit);
    }

    public void addKnowledgeBlock(KnowledgeBlock block) {
        knowledgeBlocks.add(block);
    }

    public void attachUnitToBlock(String unitCode, String blockCode, double coeff) {
        associations.add(new UnitInBlockAssociation(unitCode, blockCode, coeff));
    }
}
