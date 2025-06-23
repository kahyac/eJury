package amu.eJury.controller;

import amu.eJury.dao.*;
import amu.eJury.model.users.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/results")
@RequiredArgsConstructor
public class StudentResultController {

    private final StudentRepository studentRepository;
    private final TeachingUnitGradeRepository gradeRepository;
    private final SemestrialBlockResultRepository semestrialResultRepository;
    private final AnnualBlockResultRepository annualResultRepository;
    private final AcademicYearResultRepository yearResultRepository;

    @GetMapping("/view")
    public String showStudentSelection(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "result/student-select";
    }

    @PostMapping("/view")
    public String showStudentResults(@RequestParam Long studentId, Model model) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        model.addAttribute("student", student);

        model.addAttribute("unitGrades", gradeRepository.findByRegistration_Student_Id(studentId));
        model.addAttribute("semestrialResults", semestrialResultRepository.findByStudent(student));
        model.addAttribute("annualResults", annualResultRepository.findByStudent(student));
        model.addAttribute("yearResult", yearResultRepository.findByStudent(student).orElse(null));

        return "result/student-result";
    }
}
