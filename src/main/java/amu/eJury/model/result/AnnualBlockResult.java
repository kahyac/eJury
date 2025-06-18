package amu.eJury.model.result;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.student.Student;
import jakarta.persistence.*;
import lombok.*;

/*---------------------------------------------------------
 * 1.3  Résultat BCC Annuel
 *---------------------------------------------------------*/
@Entity
@Table(name = "annual_block_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class AnnualBlockResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne @JoinColumn(name = "annual_block_id")
    private AnnualKnowledgeBlock annualBlock;

    private Double average;
    @Enumerated(EnumType.STRING)
    private ExceptionalStatus status;
}