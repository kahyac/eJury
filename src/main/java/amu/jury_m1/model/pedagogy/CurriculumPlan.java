package amu.jury_m1.model.pedagogy;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curriculum_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * CurriculumPlan -- Entité métier représentant une maquette pédagogique annuelle structurée en blocs annuels (ex : M1, M2).
 */
public class CurriculumPlan {

    @Builder.Default
    @Id
    private Long id = 1L;  // identifiant fixe car un seul parcours géré

    private String academicYear;

    private String name;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curriculum_plan_id")
    private List<AnnualKnowledgeBlock> annualKnowledgeBlocks = new ArrayList<>();
}
