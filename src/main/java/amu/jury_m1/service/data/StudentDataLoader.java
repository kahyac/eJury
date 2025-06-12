package amu.jury_m1.service.data;


import amu.jury_m1.model.student.Student;
import amu.jury_m1.dao.StudentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.IntStream;

@Component
public class StudentDataLoader{

    private final StudentRepository studentRepository;

    public StudentDataLoader(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    public void loadStudentData() {
        if (studentRepository.count() == 0) {
            IntStream.rangeClosed(1, 30).forEach(i -> {
                Student student = new Student(
                        UUID.randomUUID().toString(),
                        "Nom" + i,
                        "Prenom" + i,
                        "etudiant" + i + "@etu.univ-amu.fr"
                );
                studentRepository.save(student);
            });
            System.out.println("30 étudiants générés dans la base H2.");
        }
    }
}