package amu.jury_m1.controller;

import amu.jury_m1.dao.CurriculumPlanRepository;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.service.curriculum.CurriculumManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
