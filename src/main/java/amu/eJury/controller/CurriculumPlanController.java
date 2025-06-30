package amu.eJury.controller;

import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.service.api.CurriculumPlanService;
import amu.eJury.service.api.TeachingUnitService;
import amu.eJury.service.dtos.CurriculumPlanDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class CurriculumPlanController {

    private final CurriculumPlanService curriculumPlanService;
    private final TeachingUnitService teachingUnitService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        boolean exists = curriculumPlanService.existsDefaultCurriculumPlan();
        model.addAttribute("dto", new CurriculumPlanDTO("", ""));
        model.addAttribute("curriculumExists", exists);
        return "curriculum/create_curriculum";
    }

    @PostMapping("/create")
    public String createCurriculum(@Valid @ModelAttribute("dto") CurriculumPlanDTO dto,
                                   BindingResult result,
                                   Model model) {
        boolean exists = curriculumPlanService.existsDefaultCurriculumPlan();
        model.addAttribute("curriculumExists", exists);

        if (result.hasErrors()) {
            return "curriculum/create_curriculum";
        }

        curriculumPlanService.createCurriculumPlan(dto);
        return "redirect:/curriculum/1";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        CurriculumPlan plan = curriculumPlanService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquette introuvable"));

        CurriculumPlanDTO dto = new CurriculumPlanDTO(plan.getAcademicYear(), plan.getName());
        model.addAttribute("dto", dto);
        model.addAttribute("planId", plan.getId());
        return "curriculum/edit_curriculum";
    }

    @PostMapping("/{id}/edit")
    public String updateCurriculum(@PathVariable Long id,
                                   @Valid @ModelAttribute("dto") CurriculumPlanDTO dto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute("planId", id);
            return "curriculum/edit_curriculum";
        }

        curriculumPlanService.updateCurriculumPlan(id, dto);

        return "redirect:/curriculum/" + id;
    }

    @GetMapping("/{id}")
    public String viewCurriculum(@PathVariable Long id, Model model) {
        CurriculumPlan plan = curriculumPlanService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curriculum not found: " + id));
        model.addAttribute("plan", plan);
        model.addAttribute("teachingUnits", teachingUnitService.findAll());

        return "curriculum/curriculum";
    }
}
