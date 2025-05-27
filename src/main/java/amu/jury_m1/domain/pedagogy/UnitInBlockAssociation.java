package amu.jury_m1.domain.pedagogy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Association between a TeachingUnit and a KnowledgeBlock for a given curriculum.
 */

@Getter
@RequiredArgsConstructor
public class UnitInBlockAssociation {
    private final String teachingUnitCode; // Code de l’UE concernée
    private final String blockCode;        // Code du BCC concerné
    private final double coefficient;      // Coefficient dans ce bloc
}
