package amu.jury_m1.domain.student;

import amu.jury_m1.domain.student.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {
}

