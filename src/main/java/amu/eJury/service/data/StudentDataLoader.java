package amu.eJury.service.data;

import amu.eJury.dao.StudentRepository;
import amu.eJury.model.users.Student;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class StudentDataLoader {

    private final StudentRepository studentRepo;

    @PostConstruct
    public void loadStudentData() {

        if (studentRepo.count() > 0) return; // Données déjà chargées

        IntStream.rangeClosed(1, 30).forEach(i -> {
            String num = String.format("%02d", i); // 01 à 30
            Student stu = Student.builder()
                    .identifiant("etudiant" + num)
                    .lastName("Nom" + num)
                    .firstName("Prenom" + num)
                    .email("etudiant" + num + "@etu.univ-amu.fr")
                    .build();
            studentRepo.save(stu);
        });

        System.out.println("✔︎ 30 étudiants générés (loader dev).");
    }
}
