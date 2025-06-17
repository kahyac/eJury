package amu.jury_m1.model.result;

import amu.jury_m1.model.registration.PedagogicalRegistration;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teaching_unit_grade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
        * La classe représente la note/statut d’un étudiant dans une UE.
 *      *
         *      * @param ueId        identifiant de la TeachingUnit
 *      * @param studentId   identifiant de l’étudiant
 *      * @param numeric     note sur 20 ; peut être {@code null} si statut exceptionnel
 *      * @param status      NONE, ABI, ABJ ou AR
 *      */
public class TeachingUnitGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK vers inscription pédagogique
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private PedagogicalRegistration registration;

    private Double grade;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ExceptionalStatus status = ExceptionalStatus.AR;   // AR par défaut
}
