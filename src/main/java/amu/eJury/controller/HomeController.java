package amu.eJury.controller;

import amu.eJury.dao.CurriculumPlanRepository;
import amu.eJury.model.pedagogy.CurriculumPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CurriculumPlanRepository curriculumRepository;

    @GetMapping("/")
    public String showHomePage(Model model) {
        Optional<CurriculumPlan> existingPlan = curriculumRepository.findById(1L);

        existingPlan.ifPresent(plan -> {
            model.addAttribute("hasCurriculum", true);
            model.addAttribute("curriculumId", plan.getId());
        });

        return "home";
    }

}