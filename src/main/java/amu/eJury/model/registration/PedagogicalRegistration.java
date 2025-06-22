package amu.eJury.model.registration;

import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import amu.eJury.model.pedagogy.TeachingUnit;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teaching_unit_code")
    private TeachingUnit teachingUnit;

    private int semester;

    @OneToOne(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeachingUnitGrade gradeInfo;

}