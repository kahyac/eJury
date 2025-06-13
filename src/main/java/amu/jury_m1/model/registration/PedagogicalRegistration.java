package amu.jury_m1.model.registration;

import amu.jury_m1.model.student.Student;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedagogical_registration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedagogicalRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teaching_unit_code")
    private TeachingUnit teachingUnit;

    private int semester;

    private Double grade;
}