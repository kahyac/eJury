package amu.jury_m1.domain.pedagogy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * TeachingUnit — Represents a UE (Unité d’Enseignement).
 */

@Getter
@RequiredArgsConstructor
public class TeachingUnit {
    private final String code;             // Code unique de l’UE
    private final String label;            // Libellé
    private final double ects;             // Crédits ECTS
    private final double workloadHours;    // Volume horaire
    private final double coefficient;      // Coefficient général
}
