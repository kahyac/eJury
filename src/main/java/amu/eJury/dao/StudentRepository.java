package amu.eJury.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import amu.eJury.model.users.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}

