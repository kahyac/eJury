package amu.eJury.service.api;

import amu.eJury.model.users.Student;

public interface BonusPolicy {
    double bonusFor(Student s, int semester);          // 0 → 0,5
}