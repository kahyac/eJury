package amu.eJury.model.users;

import amu.eJury.model.registration.PedagogicalRegistration;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedagogicalRegistration> pedagogicalRegistrations = new ArrayList<>();

    @OneToOne(mappedBy = "student")
    private AppUser appUser;
}