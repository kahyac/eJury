package amu.jury_m1.domain.pedagogy;

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
public class CurriculumPlan {

    @Id
    private String id;  // exemple : "2024-GIG" ou "2024-IDL"

    private String academicYear;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "curriculum_plan_id")
    private List<TeachingUnit> teachingUnits = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "curriculum_plan_id")
    private List<KnowledgeBlock> knowledgeBlocks = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "curriculum_plan_id")
    private List<UnitInBlockAssociation> associations = new ArrayList<>();
}
