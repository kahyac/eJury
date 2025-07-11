package amu.eJury.controller;

import amu.eJury.dao.CurriculumPlanRepository;
import amu.eJury.service.impl.JuryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jury")
public class JuryController {

    private final JuryServiceImpl juryService;
    private final CurriculumPlanRepository planRepo;

    @PostMapping("/run")
    public String launchJury(RedirectAttributes redirectAttributes) {
        Long planId = planRepo.findAll().stream()
                .findFirst()
                .orElseThrow()
                .getId();

        int count = juryService.runJury(planId);
        redirectAttributes.addFlashAttribute("message", count + " étudiants traités.");
        return "redirect:/grades/view";
    }

}
