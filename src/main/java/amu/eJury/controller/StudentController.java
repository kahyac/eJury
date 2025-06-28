package amu.eJury.controller;

import amu.eJury.dao.AppUserRepository;
import amu.eJury.dao.StudentRepository;
import amu.eJury.model.users.AppUser;
import amu.eJury.model.users.Role;
import amu.eJury.model.users.Student;
import amu.eJury.service.dtos.StudentUserDto;
import amu.eJury.service.impl.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "students/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("form", new StudentUserDto(null, "", "", ""));
        return "students/form";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("form") StudentUserDto form,
                              RedirectAttributes redirectAttributes) {
        // Crée l'étudiant
        Student student = Student.builder()
                .firstName(form.firstName())
                .lastName(form.lastName())
                .build();
        studentRepository.save(student);

        // Crée l'utilisateur associé
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        AppUser appUser = AppUser.builder()
                .email(form.email())
                .password(passwordEncoder.encode(rawPassword))
                .firstLogin(true)
                .role(Role.STUDENT)
                .student(student)
                .build();
        appUserRepository.save(appUser);

        // Envoi e-mail avec mot de passe
        emailService.sendNewUserEmail(form.email(), rawPassword);

        redirectAttributes.addFlashAttribute("successMessage", "L’étudiant a été ajouté avec succès.");

        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        studentRepository.findById(id).ifPresent(student -> {
            String email = student.getAppUser() != null ? student.getAppUser().getEmail() : "";
            model.addAttribute("form",
                    new StudentUserDto(student.getId(), student.getFirstName(), student.getLastName(), email));
        });
        return "students/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "L’étudiant a été supprimé avec succès.");
        return "redirect:/students";
    }

}
