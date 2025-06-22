package amu.eJury.model.student;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String identifiant;
    private String lastName;
    private String firstName;
    private String email;
}