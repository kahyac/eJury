package amu.eJury.controller;

import amu.eJury.dao.PedagogicalRegistrationRepository;
import amu.eJury.dao.StudentRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.model.users.Student;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.service.api.RegistrationService;
import amu.eJury.service.dtos.PedagogicalRegistrationFormDTO;
import amu.eJury.service.dtos.RegistrationWithGradeViewDTO;
import amu.eJury.service.dtos.StudentRegistrationsViewDTO;
import amu.eJury.service.impl.TeachingUnitGradeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registrations")
public class PedagogicalRegistrationController {

    private final PedagogicalRegistrationRepository registrationRepository;
    private final RegistrationService registrationService;
    private final TeachingUnitGradeServiceImpl teachingUnitGradeService;
    private final StudentRepository studentRepository;
    private final TeachingUnitRepository teachingUnitRepository;


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("form",
                new PedagogicalRegistrationFormDTO(null, null, 1));
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("optionalUnits", teachingUnitRepository.findByObligationFalse());
        return "registrations/create";
    }

    @PostMapping("/create")
    public String submitForm(@ModelAttribute PedagogicalRegistrationFormDTO form,
                             RedirectAttributes redirectAttributes) {

        Student student = studentRepository.findById(form.studentId())
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable."));
        TeachingUnit ue = teachingUnitRepository.findById(form.teachingUnitId())
                .orElseThrow(() -> new IllegalArgumentException("UE introuvable."));

        // 1. Vérifier correspondance semestre ↔ UE
        if (ue.getSemester() != form.semester()) {
            redirectAttributes.addFlashAttribute("error", "Le semestre sélectionné ne correspond pas à celui de l’UE choisie.");
            return "redirect:/registrations/create";
        }

        // 2. Vérifier si l'étudiant est déjà inscrit à cette UE
        boolean alreadyExists = registrationRepository
                .findByStudentAndTeachingUnit(student, ue)
                .isPresent();
        if (alreadyExists) {
            redirectAttributes.addFlashAttribute("error", "Cet(te) étudiant(e) est déjà inscrit(e) à cette UE.");
            return "redirect:/registrations/create";
        }

        // 3. Vérifier si ECTS dépasse 30
        List<PedagogicalRegistration> existingRegs = registrationRepository
                .findByStudentIdAndSemester(form.studentId(), form.semester());

        int currentEcts = existingRegs.stream()
                .mapToInt(reg -> (int) reg.getTeachingUnit().getEcts())
                .sum();

        int newUeEcts = (int) ue.getEcts();

        if (currentEcts + newUeEcts > 30) {
            redirectAttributes.addFlashAttribute("error", "Impossible d’ajouter cette UE : vous dépasseriez 30 ECTS pour le semestre.");
            return "redirect:/registrations/create";
        }

        // 4. Si tout est OK, enregistrer
        PedagogicalRegistration registration = PedagogicalRegistration.builder()
                .student(student)
                .teachingUnit(ue)
                .semester(form.semester())
                .build();

        // Création de la note associée avec statut AR
        TeachingUnitGrade grade = TeachingUnitGrade.builder()
                .registration(registration)
                .status(ExceptionalStatus.AR)
                .build();

        // Lien bidirectionnel
        registration.setGradeInfo(grade);

        registrationRepository.save(registration);
        redirectAttributes.addFlashAttribute("success", "Inscription pédagogique enregistrée avec succès.");
        return "redirect:/registrations";
    }




    @GetMapping
    public String getGroupedRegistrations(Model model) {
        List<PedagogicalRegistration> allRegistrations = registrationRepository.findAll();

        // Grouper les inscriptions par étudiant (par ID)
        Map<String, List<PedagogicalRegistration>> groupedByStudentId = allRegistrations.stream()
                .collect(Collectors.groupingBy(reg -> String.valueOf(reg.getStudent().getId())));

        List<StudentRegistrationsViewDTO> views = groupedByStudentId.entrySet().stream()
                .map(entry -> {
                    String studentId = entry.getKey();
                    List<PedagogicalRegistration> regs = entry.getValue();
                    String fullName = regs.get(0).getStudent().getFirstName() + " " + regs.get(0).getStudent().getLastName();
                    List<StudentRegistrationsViewDTO.UeView> ueViews = regs.stream()
                            .map(reg -> new StudentRegistrationsViewDTO.UeView(
                                    reg.getTeachingUnit().getLabel(),
                                    !reg.getTeachingUnit().isObligation(),
                                    reg.getTeachingUnit().getId()
                            ))
                            .collect(Collectors.toList());
                    return new StudentRegistrationsViewDTO(studentId, fullName, ueViews);
                })
                .collect(Collectors.toList());

        model.addAttribute("studentRegistrations", views);
        return "registrations/list";
    }

    @GetMapping("/{studentId}")
    public String getStudentNotes(@PathVariable Long studentId, Model model) {
        List<PedagogicalRegistration> regs = registrationRepository.findByStudentId(studentId);

        List<RegistrationWithGradeViewDTO> regsWithNotes = regs.stream()
                .map(reg -> new RegistrationWithGradeViewDTO(
                        reg,
                        teachingUnitGradeService.getDisplayValue(reg.getGradeInfo())
                ))
                .toList();

        model.addAttribute("registrationViews", regsWithNotes);
        return "registrations/student-notes";
    }


    @PostMapping("/{studentId}/unit/{unitId}/delete")
    public String deletePedagogicalRegistration(@PathVariable Long studentId,
                                                @PathVariable Long unitId,
                                                RedirectAttributes redirectAttributes) {
        registrationService.deleteRegistration(studentId, unitId);
        redirectAttributes.addFlashAttribute("successMessage", "Inscription supprimée avec succès.");
        return "redirect:/registrations";
    }

    @GetMapping("/check-ects")
    public String checkEcts(@RequestParam int semester, Model model) {
        Map<Student, Integer> incomplets = registrationService.checkECTSCompletenessForSemester(semester);
        model.addAttribute("semester", semester);
        model.addAttribute("incomplets", incomplets);
        return "registrations/ects-check";
    }


}