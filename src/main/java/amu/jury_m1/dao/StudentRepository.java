package amu.jury_m1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import amu.jury_m1.domain.student.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}

