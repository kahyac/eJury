package amu.jury_m1.model.student.init;


import amu.jury_m1.model.student.Student;
import amu.jury_m1.dao.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.IntStream;

@Component
public class StudentDataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public StudentDataLoader(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            IntStream.rangeClosed(1, 200).forEach(i -> {
                Student student = new Student(
                        UUID.randomUUID().toString(),
                        "Nom" + i,
                        "Prenom" + i,
                        "etudiant" + i + "@etu.univ-amu.fr"
                );
                studentRepository.save(student);
            });
            System.out.println("✅ 200 étudiants générés dans la base H2.");
        }
    }
}