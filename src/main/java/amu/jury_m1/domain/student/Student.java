package amu.jury_m1.domain.student;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Student -- Entité métier représentant un étudiant (identité + statut)
 */

@Getter
@RequiredArgsConstructor
public class Student {

    private final StudentId id;
    private final String lastName;
    private final String firstName;
    private final String email;
    private boolean archived = false;

    public void archive() {
        this.archived = true;
    }
}
