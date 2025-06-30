package amu.eJury.model.result;


import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.users.Student;
import jakarta.persistence.*;
import lombok.*;

/*---------------------------------------------------------
 * 1.2  Résultat BCC Semestriel
 *---------------------------------------------------------*/
@Entity
@Table(
        name = "sem_block_result",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "sem_block_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemestrialKnowledgeBlockResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne @JoinColumn(name = "sem_block_id")
    private SemestrialKnowledgeBlock semBlock;

    private Double average;  // null si statut exceptionnel différent de NONE

    @Enumerated(EnumType.STRING)
    private ExceptionalStatus status;

    /* bonus appliqué (0 → 0,5) */
    private double bonusApplied;
}