package amu.jury_m1.model.student;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Student -- Entité métier représentant un étudiant (identité + statut)
 */

public class Student{
    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String email;
}