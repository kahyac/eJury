package amu.jury_m1.domain.student;

import java.util.Optional;
import java.util.List;

public interface StudentRepository {
    List<Student> findActive();
}

