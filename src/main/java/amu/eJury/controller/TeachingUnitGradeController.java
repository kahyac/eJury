package amu.eJury.controller;

import amu.eJury.dao.*;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import amu.eJury.model.users.AppUser;
import amu.eJury.model.users.Teacher;
import amu.eJury.service.dtos.TeachingUnitGradeForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/grades")
@RequiredArgsConstructor
public class TeachingUnitGradeController {

    private final StudentRepository studentRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final TeachingUnitGradeRepository teachingUnitGradeRepository;
    private final PedagogicalRegistrationRepository pedagogicalRegistrationRepository;
    private final AppUserRepository appUserRepository;

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("gradeForm", new TeachingUnitGradeForm(null, null, "NONE", null)); // ✅ nécessaire
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("units", teachingUnitRepository.findAll());
        return "grade/saisie-note";
    }

    @PostMapping("/save")
    public String saveGrade(@ModelAttribute("gradeForm") TeachingUnitGradeForm form,
                            RedirectAttributes redirectAttributes) {

        PedagogicalRegistration registration = pedagogicalRegistrationRepository
                .findByStudentIdAndTeachingUnitId(form.studentId(), form.unitId())
                .orElseThrow(() -> new IllegalArgumentException("Inscription pédagogique introuvable"));

        TeachingUnitGrade grade = registration.getGradeInfo();

        if (grade == null) {
            grade = TeachingUnitGrade.builder()
                    .registration(registration)
                    .build();
        }

        grade.setStatus(ExceptionalStatus.valueOf(form.status()));
        grade.setGrade(form.status().equals("NONE") ? form.numeric() : null);

        teachingUnitGradeRepository.save(grade);

        redirectAttributes.addFlashAttribute("successMessage", "Note ou statut enregistré(e) avec succès.");
        return "redirect:/grades/new";
    }

    @GetMapping("/view")
    public String showGradeLookupForm(Model model,
                                      @ModelAttribute("currentStudent") Student currentStudent,
                                      @ModelAttribute("currentUser") AppUser currentUser) {

        if (currentStudent != null) {
            // Cas étudiant
            model.addAttribute("students", List.of(currentStudent));
            model.addAttribute("selectedStudentId", currentStudent.getId());
            model.addAttribute("units", teachingUnitRepository.findAll());
        } else if (currentUser != null && currentUser.getTeacher() != null) {
            // Cas enseignant ou admin responsable d’UE
            Teacher teacher = currentUser.getTeacher();
            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("units", teacher.getTeachingUnits()); // juste ses UEs
        } else {
            // Cas admin sans rôle enseignant
            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("units", teachingUnitRepository.findAll());
        }

        return "grade/view-grades";
    }



    @PostMapping("/view")
    public String displayGrade(@RequestParam Long studentId,
                               @RequestParam Long unitId,
                               @ModelAttribute("currentStudent") Student currentStudent,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (currentStudent != null && !studentId.equals(currentStudent.getId())) {
            redirectAttributes.addFlashAttribute("message", "Accès non autorisé.");
            return "redirect:/grades/view";
        }

        var registrationOpt = pedagogicalRegistrationRepository
                .findByStudentIdAndTeachingUnitId(studentId, unitId);

        if (registrationOpt.isEmpty() || registrationOpt.get().getGradeInfo() == null) {
            model.addAttribute("message", "Aucune note disponible pour cette UE.");
            model.addAttribute("grade", null);
        } else {
            var grade = registrationOpt.get().getGradeInfo();
            model.addAttribute("grade", grade);
        }

        model.addAttribute("students", currentStudent != null
                ? java.util.List.of(currentStudent)
                : studentRepository.findAll());

        model.addAttribute("units", teachingUnitRepository.findAll());
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("selectedUnitId", unitId);

        return "grade/view-grades";
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
