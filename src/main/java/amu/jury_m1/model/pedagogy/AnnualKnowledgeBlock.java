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
    private String id;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "year_block_id")
    private List<SemestrialKnowledgeBlock> semesters = new ArrayList<>();
}
