package amu.eJury.model.pedagogy;
import amu.eJury.model.users.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentBonus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    private int semester;

    @Column(nullable = false)
    private double value;
}
