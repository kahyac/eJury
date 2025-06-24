package amu.eJury.controller;

import amu.eJury.dao.AppUserRepository;
import amu.eJury.model.users.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class PasswordController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String handlePasswordChange(@RequestParam("newPassword") String newPassword, Principal principal) {
        AppUser user = appUserRepository.findByEmail(principal.getName()).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);
        appUserRepository.save(user);
        return "redirect:/";
    }
}
