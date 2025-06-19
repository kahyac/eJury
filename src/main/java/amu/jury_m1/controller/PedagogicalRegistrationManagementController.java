package amu.jury_m1.controller;

import amu.jury_m1.dao.*;
import amu.jury_m1.model.registration.PedagogicalRegistration;
import amu.jury_m1.model.result.ExceptionalStatus;
import amu.jury_m1.model.result.TeachingUnitGrade;
import amu.jury_m1.model.student.Student;
import amu.jury_m1.model.pedagogy.TeachingUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class PedagogicalRegistrationManagementController {

    private final StudentRepository studentRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final PedagogicalRegistrationRepository registrationRepository;
    private final TeachingUnitGradeRepository gradeRepository;

    @GetMapping("/edit/{studentId}")
    public String editRegistrations(@PathVariable String studentId, Model model) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        List<PedagogicalRegistration> registrations = registrationRepository.findByStudentId(studentId);
        List<TeachingUnit> allUnits = teachingUnitRepository.findAll();

        model.addAttribute("student", student);
        model.addAttribute("registrations", registrations);
        model.addAttribute("allUnits", allUnits);
        model.addAttribute("statuses", ExceptionalStatus.values());
        return "registrations/manage";
    }

    @PostMapping("/{studentId}/save")
    public String saveRegistration(@PathVariable String studentId,
                                   @RequestParam Long ueId,
                                   @RequestParam Integer semester,
                                   @RequestParam(required = false) Double grade,
                                   @RequestParam ExceptionalStatus status,
                                   RedirectAttributes redirectAttributes) {

        // 🛑 Validation 1 : note obligatoire si le statut est "NONE"
        if (status == ExceptionalStatus.NONE && grade == null) {
            redirectAttributes.addFlashAttribute("error", "Une note est requise si le statut est 'Aucune exception'.");
            return "redirect:/registrations/" + studentId + "/manage";
        }

        // ✅ Validation 2 : efface la note si un statut exceptionnel est choisi
        if (status != ExceptionalStatus.NONE) {
            grade = null;
        }

        // 🔎 Récupérer les entités nécessaires
        TeachingUnit ue = teachingUnitRepository.findById(ueId).orElseThrow();
        Student student = studentRepository.findById(studentId).orElseThrow();

        // 🏗 Rechercher une IP existante ou en créer une nouvelle
        Optional<PedagogicalRegistration> existing = registrationRepository.findByStudentIdAndTeachingUnitId(studentId, ueId);

        PedagogicalRegistration reg = existing.orElse(PedagogicalRegistration.builder()
                .student(student)
                .teachingUnit(ue)
                .semester(semester)
                .build());

        reg.setSemester(semester);
        registrationRepository.save(reg);

        // 💾 Enregistrer ou mettre à jour la note associée
        TeachingUnitGrade gradeEntity = gradeRepository.findByRegistration(reg)
                .orElse(TeachingUnitGrade.builder().registration(reg).build());

        gradeEntity.setGrade(grade);
        gradeEntity.setStatus(status);
        gradeRepository.save(gradeEntity);

        // ✅ Feedback utilisateur
        redirectAttributes.addFlashAttribute("success", "Inscription mise à jour.");
        return "redirect:/registrations/" + studentId ;
    }

    @GetMapping("/delete/{regId}")
    public String deleteRegistration(@PathVariable String regId) {
        PedagogicalRegistration reg = registrationRepository.findById(regId).orElseThrow();
        gradeRepository.delete(reg.getGrade());
        registrationRepository.delete(reg);
        return "redirect:/registrations/edit/" + reg.getStudent().getId();
    }
}