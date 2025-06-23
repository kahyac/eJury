package amu.eJury.controller;

import amu.eJury.dao.PedagogicalRegistrationRepository;
import amu.eJury.dao.StudentRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.RegistrationService;
import amu.eJury.service.dtos.PedagogicalRegistrationForm;
import amu.eJury.service.dtos.RegistrationWithGradeView;
import amu.eJury.service.dtos.StudentRegistrationsView;
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
public class RegistrationController {

    private final PedagogicalRegistrationRepository registrationRepository;
    private final RegistrationService registrationService;
    private final TeachingUnitGradeServiceImpl teachingUnitGradeService;
    private final StudentRepository studentRepository;
    private final TeachingUnitRepository teachingUnitRepository;


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("form", new PedagogicalRegistrationForm(null, null, 1));
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("optionalUnits", teachingUnitRepository.findByObligationFalse());
        return "registrations/create";
    }

    @PostMapping("/create")
    public String submitForm(@ModelAttribute PedagogicalRegistrationForm form) {
        Student student = studentRepository.findById(form.studentId()).orElseThrow();
        TeachingUnit ue = teachingUnitRepository.findById(form.teachingUnitId()).orElseThrow();

        PedagogicalRegistration registration = PedagogicalRegistration.builder()
                .student(student)
                .teachingUnit(ue)
                .semester(form.semester())
                .build();

        registrationRepository.save(registration);
        return "redirect:/registrations";
    }

    @GetMapping
    public String getGroupedRegistrations(Model model) {
        List<PedagogicalRegistration> allRegistrations = registrationRepository.findAll();

        // Grouper les inscriptions par étudiant (par ID)
        Map<String, List<PedagogicalRegistration>> groupedByStudentId = allRegistrations.stream()
                .collect(Collectors.groupingBy(reg -> String.valueOf(reg.getStudent().getId())));

        List<StudentRegistrationsView> views = groupedByStudentId.entrySet().stream()
                .map(entry -> {
                    String studentId = entry.getKey();
                    List<PedagogicalRegistration> regs = entry.getValue();
                    String fullName = regs.get(0).getStudent().getFirstName() + " " + regs.get(0).getStudent().getLastName();
                    List<StudentRegistrationsView.UeView> ueViews = regs.stream()
                            .map(reg -> new StudentRegistrationsView.UeView(
                                    reg.getTeachingUnit().getLabel(),
                                    !reg.getTeachingUnit().isObligation(),
                                    reg.getTeachingUnit().getId()
                            ))
                            .collect(Collectors.toList());
                    return new StudentRegistrationsView(studentId, fullName, ueViews);
                })
                .collect(Collectors.toList());

        model.addAttribute("studentRegistrations", views);
        return "registrations/list";
    }

    @GetMapping("/{studentId}")
    public String getStudentNotes(@PathVariable Long studentId, Model model) {
        List<PedagogicalRegistration> regs = registrationRepository.findByStudentId(studentId);

        List<RegistrationWithGradeView> regsWithNotes = regs.stream()
                .map(reg -> new RegistrationWithGradeView(
                        reg,
                        teachingUnitGradeService.getDisplayValue(reg.getGradeInfo())
                ))
                .toList();

        model.addAttribute("registrationViews", regsWithNotes);
        return "registrations/student-notes";
    }


    @PostMapping("/registrations/{studentId}/unit/{unitId}/delete")
    public String deletePedagogicalRegistration(@PathVariable Long studentId,
                                                @PathVariable Long unitId,
                                                RedirectAttributes redirectAttributes) {
        registrationService.deleteRegistration(studentId, unitId);
        redirectAttributes.addFlashAttribute("successMessage", "Inscription supprimée avec succès.");
        return "redirect:/registrations";
    }

    @GetMapping("/check-ects")
    public String checkEcts(@RequestParam int semester, Model model) {
        Map<Student, Integer> incomplets = registrationService.checkEctsCompletenessForSemester(semester);
        model.addAttribute("semester", semester);
        model.addAttribute("incomplets", incomplets);
        return "registrations/ects-check";
    }


}