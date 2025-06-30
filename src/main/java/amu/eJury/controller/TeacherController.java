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

        if (form.id() != null) {
            // MODIFICATION d’un enseignant existant
            Teacher teacher = teacherRepository.findById(form.id())
                    .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé"));

            teacher.setFirstName(form.firstName());
            teacher.setLastName(form.lastName());
            teacherRepository.save(teacher);

            AppUser user = teacher.getAppUser();
            if (user != null) {
                user.setEmail(form.email()); // même si doublon possible, on laisse passer
                appUserRepository.save(user);
            }

            // Réinitialisation de l'UE responsable précédente si elle existe
            teachingUnitRepository.findByResponsibleTeacherId(teacher.getId())
                    .ifPresent(oldUe -> {
                        oldUe.setResponsibleTeacher(null);
                        teachingUnitRepository.save(oldUe);
                    });

            // Attribution de la nouvelle UE si sélectionnée
            if (form.ueId() != null) {
                TeachingUnit ue = teachingUnitRepository.findById(form.ueId()).orElseThrow();
                ue.setResponsibleTeacher(teacher);
                teachingUnitRepository.save(ue);
            }

            redirectAttributes.addFlashAttribute("successMessage", "L’enseignant a été mis à jour avec succès.");
        } else {
            // CRÉATION d’un nouvel enseignant
            Teacher teacher = Teacher.builder()
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

            if (form.ueId() != null) {
                TeachingUnit ue = teachingUnitRepository.findById(form.ueId()).orElseThrow();
                ue.setResponsibleTeacher(teacher);
                teachingUnitRepository.save(ue);
            }

            emailService.sendNewUserEmail(form.email(), rawPassword);
            redirectAttributes.addFlashAttribute("successMessage", "L’enseignant a été ajouté avec succès.");
        }

        return "redirect:/teachers";
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        teacherRepository.findById(id).ifPresent(teacher -> {
            String email = teacher.getAppUser() != null ? teacher.getAppUser().getEmail() : "";
            Long ueId = teachingUnitRepository.findByResponsibleTeacherId(teacher.getId())
                    .map(TeachingUnit::getId)
                    .orElse(null);
            model.addAttribute("form", new TeacherDTO(
                    teacher.getId(),
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    email,
                    ueId
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
