package amu.eJury.model.users;

import amu.eJury.model.pedagogy.TeachingUnit;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToOne(mappedBy = "teacher")
    private AppUser appUser;

    @OneToMany(mappedBy = "responsibleTeacher")
    private List<TeachingUnit> teachingUnits; // UEs dont il est responsable
}
