package amu.eJury.controller;

import amu.eJury.dao.StudentBonusRepository;
import amu.eJury.dao.StudentRepository;
import amu.eJury.model.pedagogy.StudentBonus;
import amu.eJury.model.users.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BonusController {

    private final StudentRepository studentRepo;
    private final StudentBonusRepository bonusRepo;

    @PostMapping("/bonus/update")
    public String updateBonus(@RequestParam Long studentId,
                              @RequestParam double bonus1,
                              @RequestParam double bonus2) {

        Student student = studentRepo.findById(studentId).orElseThrow();

        saveOrUpdateBonus(student, 1, bonus1);
        saveOrUpdateBonus(student, 2, bonus2);

        return "redirect:/registrations";
    }

    private void saveOrUpdateBonus(Student student, int semester, double value) {
        bonusRepo.findByStudentAndSemester(student, semester).ifPresentOrElse(
                existing -> {
                    existing.setValue(value);
                    bonusRepo.save(existing);
                },
                () -> bonusRepo.save(StudentBonus.builder()
                        .student(student)
                        .semester(semester)
                        .value(value)
                        .build())
        );
    }
}
