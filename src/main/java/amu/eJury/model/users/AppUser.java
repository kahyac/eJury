package amu.eJury.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // email par ex.
    private String password;

    private boolean mustResetPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Long linkedEntityId; // pour relier à Student ou Teacher selon le rôle
}
