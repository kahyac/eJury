package amu.eJury.controller;

import amu.eJury.dao.PedagogicalRegistrationRepository;
import amu.eJury.dao.StudentRepository;
import amu.eJury.dao.TeachingUnitGradeRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.service.dtos.TeachingUnitGradeForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/grades")
@RequiredArgsConstructor
public class TeachingUnitGradeController {

    private final StudentRepository studentRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final TeachingUnitGradeRepository teachingUnitGradeRepository;
    private final PedagogicalRegistrationRepository pedagogicalRegistrationRepository;

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
    public String showGradeLookupForm(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("units", teachingUnitRepository.findAll());
        return "grade/view-grades";
    }

    @PostMapping("/view")
    public String displayGrade(@RequestParam Long studentId,
                               @RequestParam Long unitId,
                               Model model) {
        var registrationOpt = pedagogicalRegistrationRepository
                .findByStudentIdAndTeachingUnitId(studentId, unitId);

        if (registrationOpt.isEmpty() || registrationOpt.get().getGradeInfo() == null) {
            model.addAttribute("message", "Aucune note disponible pour cette UE.");
            model.addAttribute("grade", null);
        } else {
            var grade = registrationOpt.get().getGradeInfo();
            model.addAttribute("grade", grade);
        }

        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("units", teachingUnitRepository.findAll());
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("selectedUnitId", unitId);

        return "grade/view-grades";
    }

}
