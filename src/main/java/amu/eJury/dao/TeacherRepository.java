package amu.eJury.dao;

import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
