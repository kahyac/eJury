package amu.jury_m1.model.pedagogy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teaching_unit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * (UE) : TeachingUnit -- Entité métier représentant une unité d'enseigement
 */
public class TeachingUnit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String label;

    private double ects;

    private double workloadHours;

    private boolean obligation;
}
