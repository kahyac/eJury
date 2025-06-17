package amu.jury_m1.model.result;

import amu.jury_m1.model.student.Student;
import jakarta.persistence.*;
import lombok.*;

/**
 * AnnualAcademicResult — Représente le résultat global d'un étudiant sur une année universitaire.
 * Contient la décision du jury (ADM, AJ) et la mention (P, AB, B, TB, EX).
 */
/*---------------------------------------------------------
 * 1.4  Résultat Année
 *---------------------------------------------------------*/
@Entity
@Table(name = "year_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;

    private String academicYear;  // ex : 2024/2025

    @Enumerated(EnumType.STRING)
    private AcademicDecision decision;  // ADM / AJ / ABJ / ABI / AR
}