package amu.jury_m1.model.result;

import amu.jury_m1.model.registration.PedagogicalRegistration;
import jakarta.persistence.*;
import lombok.*;


/*---------------------------------------------------------
 * 1.1  Note / Teaching-Unit Result
 *---------------------------------------------------------*/
@Entity
@Table(name = "teaching_unit_grade")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeachingUnitGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* FK vers inscription pédagogique                 */
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private PedagogicalRegistration registration;

    /* note de 0-20 ; null si statut exceptionnel       */
    private Double grade;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ExceptionalStatus status = ExceptionalStatus.AR;   // AR par défaut
}
