package amu.jury_m1.service.api;

import amu.jury_m1.model.student.Student;

public interface BonusPolicy {
    double bonusFor(Student s, int semester);          // 0 → 0,5
}