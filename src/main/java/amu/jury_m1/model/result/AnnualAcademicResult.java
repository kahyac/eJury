package amu.jury_m1.domain.result;

import amu.jury_m1.domain.student.StudentId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Year;

/**
 * AnnualAcademicResult — Représente le résultat global d'un étudiant sur une année universitaire.
 * Contient la décision du jury (ADM, AJ) et la mention (P, AB, B, TB, EX).
 */

@Getter
@RequiredArgsConstructor
public class AnnualAcademicResult {

    private final StudentId studentId;
    private final Year academicYear;
    private final double annualAverage;
    private final AnnualDecision decision;
    private final Mention mention;

}
