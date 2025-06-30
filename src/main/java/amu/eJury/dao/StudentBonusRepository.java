package amu.eJury.dao;

import amu.eJury.model.pedagogy.StudentBonus;
import amu.eJury.model.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentBonusRepository extends JpaRepository<StudentBonus, Long> {
    Optional<StudentBonus> findByStudentAndSemester(Student student, int semester);
}

