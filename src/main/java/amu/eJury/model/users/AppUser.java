package amu.eJury.model.users;

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

    @Column(unique = true)
    private String email;

    private String password;

    private boolean firstLogin;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Student student;

    @OneToOne
    private Teacher teacher;
}
