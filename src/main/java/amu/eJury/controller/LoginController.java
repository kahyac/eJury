package amu.eJury.controller;

import amu.eJury.service.dtos.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /** Affiche la page de connexion sans logique d'authentification */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Prépare le DTO pour le formulaire Thymeleaf
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }
}