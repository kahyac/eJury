package amu.jury_m1.model.student;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String id) {
        super("Étudiant introuvable : " + id);
    }
}
