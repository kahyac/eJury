package amu.jury_m1.model.pedagogy;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * (BCC annuel) AnnualKnowledgeBlock -- Entité métier représentant un bloc de connaissances annuel pour ses deux blocs semstriels.
 */
@Entity
@Table(name = "annual_knowledge_block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AnnualKnowledgeBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "curriculum_plan_id")
    private CurriculumPlan curriculumPlan;

    @Builder.Default
    @OneToMany(mappedBy = "annualKnowledgeBlock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SemestrialKnowledgeBlock> semesters = new ArrayList<>();

}
