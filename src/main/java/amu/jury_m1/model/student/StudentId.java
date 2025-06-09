package amu.jury_m1.model.student;

import lombok.Getter;
import java.util.UUID;
import java.util.Objects;

/**
 * StudentId -- Identifiant unique d'étudiant
 */

@Getter
public class StudentId {
    private final UUID value;

    public StudentId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public static StudentId newId() {
        return new StudentId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StudentId other = (StudentId) obj;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}