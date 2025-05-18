package amu.jury_m1.domain.student;

import java.util.Optional;
import java.util.List;

public interface StudentRepository {
    void save(Student student);
    Optional<Student> findById(StudentId id);
    List<Student> findActive();
}

