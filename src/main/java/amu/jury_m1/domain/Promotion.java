package amu.jury_m1.domain;

import amu.jury_m1.domain.student.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.Year;
import java.util.List;

/**
 * Promotion -- Représente une cohorte d'étudiants inscrits dans une même année et parcours
 */
@Getter
@RequiredArgsConstructor
public class Promotion {

    private final Year academicYear;       // année universitaire (ex: 2024)
    private final String trackLabel;       // libellé du parcours (ex: M1 IDL)
    private final List<Student> students;  // liste des étudiants de la promo
}