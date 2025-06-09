package amu.jury_m1.model.pedagogy;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "semestrial_knowledge_block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * (BCC semestriel) SemestrialKnowledgeBlock -- Entité métier représentant un bloc de connaissances pour un semestre
 */
public class SemestrialKnowledgeBlock {

    @Id
    private String code;

    private String label;

    @Min(1)
    @Max(2)
    private int semester;

    private double ects;

    @ElementCollection
    @CollectionTable(name = "unit_coefficients", joinColumns = @JoinColumn(name = "block_code"))
    @MapKeyJoinColumn(name = "teaching_unit_code")
    @Column(name = "coefficient")
    private Map<TeachingUnit, Double> unitsCoefficientAssociation = new HashMap<>();

}
