package amu.eJury.controller;

import amu.eJury.dao.AppUserRepository;
import amu.eJury.dao.TeacherRepository;
import amu.eJury.dao.TeachingUnitRepository;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.users.AppUser;
import amu.eJury.model.users.Role;
import amu.eJury.model.users.Teacher;
import amu.eJury.service.dtos.TeacherDto;
import amu.eJury.service.impl.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("form", new TeacherDto(null, "", "", "", null));
        model.addAttribute("ues", teachingUnitRepository.findAll());
        return "teachers/form";
    }

    @PostMapping("/save")
    public String saveTeacher(@ModelAttribute("form") TeacherDto form) {
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

        TeachingUnit ue = teachingUnitRepository.findById(form.ueId()).orElseThrow();
        ue.setResponsibleTeacher(teacher);
        teachingUnitRepository.save(ue);

        emailService.sendNewUserEmail(form.email(), rawPassword);

        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        teacherRepository.findById(id).ifPresent(teacher -> {
            String email = teacher.getAppUser() != null ? teacher.getAppUser().getEmail() : "";
            Long ueId = teachingUnitRepository.findByResponsibleTeacherId(teacher.getId())
                    .map(TeachingUnit::getId)
                    .orElse(null);
            model.addAttribute("form", new TeacherDto(
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
    public String deleteTeacher(@PathVariable Long id) {
        teacherRepository.deleteById(id);
        return "redirect:/teachers";
    }
}
