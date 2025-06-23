package amu.eJury.model.result;

import amu.eJury.model.users.Student;
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
@Table(
        name = "year_result",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id"})
)
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


    @Enumerated(EnumType.STRING)
    private AcademicDecision decision;  // ADM / AJ / ABJ / ABI / AR

    @Enumerated(EnumType.STRING)
    private Mention mention;
}