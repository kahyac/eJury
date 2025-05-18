package amu.jury_m1.domain.student;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String id) {
        super("Étudiant introuvable : " + id);
    }
}
