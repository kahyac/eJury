package amu.jury_m1.model.student;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Student -- Entité métier représentant un étudiant (identité + statut)
 */

public class Student{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String identifiant;
    private String lastName;
    private String firstName;
    private String email;
}