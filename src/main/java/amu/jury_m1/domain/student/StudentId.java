package amu.jury_m1.domain.student;

import java.util.UUID;
import java.util.Objects;

public record StudentId(UUID value) {
    public StudentId {
        Objects.requireNonNull(value, "StudentId cannot be null");
    }

    public static StudentId newId() {
        return new StudentId(UUID.randomUUID());
    }
}
