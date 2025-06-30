package amu.eJury.controller;

import amu.eJury.service.dtos.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /** Affiche la page de connexion sans logique d'authentification */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDTO());
        return "login";
    }
}