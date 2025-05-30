package amu.jury_m1.domain.pedagogy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teaching_unit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingUnit {

    @Id
    private String code;  // Code unique

    private String label;

    private double ects;

    private double workloadHours;
}
