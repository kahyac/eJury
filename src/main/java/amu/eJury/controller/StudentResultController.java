package amu.eJury.controller;

import amu.eJury.dao.*;
import amu.eJury.model.users.AppUser;
import amu.eJury.model.users.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/results")
@RequiredArgsConstructor
public class StudentResultController {

    private final StudentRepository studentRepository;
    private final TeachingUnitGradeRepository gradeRepository;
    private final SemestrialKnowledgeBlockResultRepository semestrialResultRepository;
    private final AnnualKnowledgeBlockResultRepository annualResultRepository;
    private final AcademicYearResultRepository yearResultRepository;
    private final AppUserRepository appUserRepository;

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
        model.addAttribute("studentId", studentId);
        return "result/student-result";
    }

    @ModelAttribute("currentStudent")
    public Student getCurrentStudent(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return null;

        return appUserRepository.findByEmail(userDetails.getUsername())
                .map(AppUser::getStudent)
                .orElse(null);
    }

    @ModelAttribute("currentUser")
    public AppUser getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return null;
        return appUserRepository.findByEmail(userDetails.getUsername()).orElse(null);
    }

}
