package amu.jury_m1.controller;

import amu.jury_m1.dao.PedagogicalRegistrationRepository;
import amu.jury_m1.model.registration.PedagogicalRegistration;
import amu.jury_m1.service.dtos.StudentRegistrationsView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    private final PedagogicalRegistrationRepository registrationRepository;

    public RegistrationController(PedagogicalRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
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
                                    !reg.getTeachingUnit().isObligation()
                            ))
                            .collect(Collectors.toList());
                    return new StudentRegistrationsView(studentId, fullName, ueViews);
                })
                .collect(Collectors.toList());

        model.addAttribute("studentRegistrations", views);
        return "registrations/list";
    }

    @GetMapping("/{studentId}")
    public String getStudentNotes(@PathVariable String studentId, Model model) {
        List<PedagogicalRegistration> studentRegistrations = registrationRepository.findByStudentId(studentId);
        model.addAttribute("registrations", studentRegistrations);
        return "registrations/student-notes";
    }
}