package amu.eJury.controller;

import amu.eJury.dao.AppUserRepository;
import amu.eJury.dao.TeacherRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.users.AppUser;
import amu.eJury.model.users.Role;
import amu.eJury.model.users.Teacher;
import amu.eJury.service.dtos.TeacherDTO;
import amu.eJury.service.impl.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "teachers/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("form", new TeacherDTO(null, "", "", "", null));
        model.addAttribute("ues", teachingUnitRepository.findAll());
        return "teachers/form";
    }

    @PostMapping("/save")
    public String saveTeacher(@ModelAttribute("form") TeacherDTO form,
                              RedirectAttributes redirectAttributes) {

        Teacher teacher;

        if (form.id() != null) {
            teacher = teacherRepository.findById(form.id())
                    .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé"));

            teacher.setFirstName(form.firstName());
            teacher.setLastName(form.lastName());
            teacherRepository.save(teacher);

            AppUser user = teacher.getAppUser();
            if (user != null) {
                user.setEmail(form.email());
                appUserRepository.save(user);
            }

            // Nettoyer toutes les UEs de cet enseignant
            List<TeachingUnit> currentUnits = teachingUnitRepository.findByResponsibleTeacherId(teacher.getId()).stream().toList();
            for (TeachingUnit ue : currentUnits) {
                ue.setResponsibleTeacher(null);
                teachingUnitRepository.save(ue);
            }

        } else {
            teacher = Teacher.builder()
                    .firstName(form.firstName())
                    .lastName(form.lastName())
                    .build();
            teacherRepository.save(teacher);

            String rawPassword = UUID.randomUUID().toString().substring(0, 8);
            AppUser appUser = AppUser.builder()
                    .email(form.email())
                    .password(passwordEncoder.encode(rawPassword))
                    .firstLogin(true)
                    .role(Role.TEACHER)
                    .teacher(teacher)
                    .build();
            appUserRepository.save(appUser);

            emailService.sendNewUserEmail(form.email(), rawPassword);
        }

        // Affecter les nouvelles UEs sélectionnées
        if (form.ueIds() != null) {
            for (Long ueId : form.ueIds()) {
                TeachingUnit ue = teachingUnitRepository.findById(ueId).orElseThrow();
                ue.setResponsibleTeacher(teacher);
                teachingUnitRepository.save(ue);
            }
        }

        redirectAttributes.addFlashAttribute("successMessage",
                form.id() != null ? "L’enseignant a été mis à jour avec succès." : "L’enseignant a été ajouté avec succès.");

        return "redirect:/teachers";
    }




    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        teacherRepository.findById(id).ifPresent(teacher -> {
            String email = teacher.getAppUser() != null ? teacher.getAppUser().getEmail() : "";
            List<Long> ueIds = teacher.getTeachingUnits().stream()
                    .map(TeachingUnit::getId)
                    .toList();
            model.addAttribute("form", new TeacherDTO(
                    teacher.getId(),
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    email,
                    ueIds
            ));
        });
        model.addAttribute("ues", teachingUnitRepository.findAll());
        return "teachers/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        teacherRepository.findById(id).ifPresent(teacher -> {

            AppUser user = teacher.getAppUser();
            if (user != null) {
                appUserRepository.delete(user);
            }

            teachingUnitRepository.findByResponsibleTeacherId(teacher.getId())
                    .ifPresent(ue -> {
                        ue.setResponsibleTeacher(null);
                        teachingUnitRepository.save(ue);
                    });

            teacherRepository.delete(teacher);
        });

        redirectAttributes.addFlashAttribute("successMessage", "L’enseignant a été supprimé avec succès.");
        return "redirect:/teachers";
    }

}
