package amu.eJury.model.users;

import jakarta.persistence.*;
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
    private Long id;

    private String identifiant;
    private String lastName;
    private String firstName;

    @OneToOne(mappedBy = "student")
    private AppUser appUser;
}