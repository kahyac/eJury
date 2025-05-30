package amu.jury_m1.domain.pedagogy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "knowledge_block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgeBlock {

    @Id
    private String code;

    private String label;

    private int semester;

    private double ects;


}
