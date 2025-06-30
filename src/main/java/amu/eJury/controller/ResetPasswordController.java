package amu.eJury.controller;

import amu.eJury.dao.AppUserRepository;
import amu.eJury.dao.PasswordResetTokenRepository;
import amu.eJury.model.PasswordResetToken;
import amu.eJury.model.users.AppUser;
import amu.eJury.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final AppUserRepository appUserRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPasswordRequest(@RequestParam("email") String email,
                                             RedirectAttributes redirectAttributes) {
        AppUser user = appUserRepository.findByEmail(email).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Adresse inconnue.");
            return "redirect:/reset-password";
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiration(LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/reset-password/confirm?token=" + token;
        emailService.sendResetPasswordLink(email, resetLink);

        redirectAttributes.addFlashAttribute("successMessage", "Un lien a été envoyé.");
        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password/confirm")
    public String showConfirmForm(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .filter(t -> t.getExpiration().isAfter(LocalDateTime.now()))
                .orElse(null);
        if (resetToken == null) return "redirect:/reset-password?expired";
        model.addAttribute("token", token);
        return "reset-password-form";
    }

    @PostMapping("/reset-password/confirm")
    public String handleReset(@RequestParam String token,
                              @RequestParam String newPassword,
                              RedirectAttributes redirectAttributes) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        if (resetToken == null || resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lien expiré.");
            return "redirect:/reset-password";
        }

        AppUser user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);
        appUserRepository.save(user);
        tokenRepository.delete(resetToken);

        redirectAttributes.addFlashAttribute("successMessage", "Mot de passe modifié.");
        return "redirect:/login";
    }

}
