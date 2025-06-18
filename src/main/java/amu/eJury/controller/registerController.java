package amu.eJury.controller;

import amu.eJury.service.dtos.RegisterDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class registerController {

    /** Affiche le formulaire d'inscription */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";  // correspond à register.html
    }

    /** Traite la soumission du formulaire d'inscription */
    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute("registerDto") @Valid RegisterDto registerDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register"; // réaffiche le formulaire en cas d'erreurs
        }

        // TODO: ajouter la logique de création d'utilisateur

        return "redirect:/login?registered";  // redirige vers la page de connexion
    }
}
