package amu.eJury.service.impl;

import amu.eJury.dao.StudentBonusRepository;
import amu.eJury.model.users.Student;
import amu.eJury.model.pedagogy.StudentBonus;
import amu.eJury.service.api.BonusPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBonusPolicy implements BonusPolicy {

    private final StudentBonusRepository bonusRepo;

    @Override
    public double bonusFor(Student s, int semester) {
        return bonusRepo.findByStudentAndSemester(s, semester)
                .map(StudentBonus::getValue)
                .orElse(0.0);
    }
}
