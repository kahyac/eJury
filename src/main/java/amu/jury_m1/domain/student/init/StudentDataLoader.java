package amu.jury_m1.domain.student.init;

import amu.jury_m1.domain.student.Student;
import amu.jury_m1.dao.StudentRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class StudentDataLoader {

    private final StudentRepository studentRepository;

    public StudentDataLoader(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    public void loadStudentData() {
        if (studentRepository.count() == 0) {
            IntStream.rangeClosed(1, 50).forEach(i -> {
                Student student = new Student(
                        UUID.randomUUID().toString(),
                        "Nom" + i,
                        "Prenom" + i,
                        "etudiant" + i + "@etu.univ-amu.fr"
                );
                studentRepository.save(student);
            });
            System.out.println("✅ 50 étudiants générés dans la base H2.");
        }
    }
}