package amu.jury_m1.controller;

import amu.jury_m1.dao.TeachingUnitRepository;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.service.curriculum.CurriculumManagementService;
import amu.jury_m1.service.dtos.*;
import amu.jury_m1.dao.CurriculumPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Web controller for managing the curriculum through Thymeleaf views.
 * Handles Teaching Units, Knowledge Blocks, and Associations.
 */
@Controller
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class CurriculumController {

    private final CurriculumPlanRepository curriculumRepo;
    private final TeachingUnitRepository teachingUnitRepository;
    private final CurriculumManagementService service;


    @GetMapping("/{id}/add-annual-block")
    public String showAddAnnualBlockForm(@PathVariable Long id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("annualBlockDto", new AnnualKnowledgeBlockDto("")); // simple DTO
        return "curriculum/add_annual_block";
    }

    @PostMapping("/{id}/add-annual-block")
    public String addAnnualBlock(@PathVariable Long id,
                                 @ModelAttribute AnnualKnowledgeBlockDto dto) {
        service.addAnnualKnowledgeBlock(dto);
        return "redirect:/curriculum/" + id;
    }

    @GetMapping("/annual/{annualId}/add-sem-block")
    public String showAddSemBlockForm(@PathVariable String annualId, Model model) {
        model.addAttribute("annualId", annualId);
        model.addAttribute("semestrialDto", new SemestrialKnowledgeBlockDto("", "", 1, 0.0));
        return "curriculum/add_sem_block";
    }

    @PostMapping("/annual/{annualId}/add-sem-block")
    public String addSemBlock(@PathVariable String annualId,
                              @ModelAttribute SemestrialKnowledgeBlockDto dto) {
        service.addSemestrialBlockToAnnual(annualId, dto);
        return "redirect:/curriculum/1";
    }

    @GetMapping("/sem-block/{blockCode}/add-unit")
    public String showAddUnitToBlock(@PathVariable String blockCode, Model model) {
        model.addAttribute("blockCode", blockCode);
        model.addAttribute("unitCodes", teachingUnitRepository.findAll());
        model.addAttribute("form", new UnitAssociationFormDto("", 1.0));
        return "curriculum/associate_unit";
    }

    @PostMapping("/sem-block/{blockCode}/add-unit")
    public String associateUnit(@PathVariable String blockCode,
                                @RequestParam String unitCode,
                                @RequestParam double coefficient) {
        service.associateTeachingUnitToSemestrialBlock(blockCode, unitCode, coefficient);
        return "redirect:/curriculum/1";
    }


    // --- Display curriculum details ---
    @GetMapping("/{id}")
    public String viewCurriculum(@PathVariable Long id, Model model) {
        CurriculumPlan plan = curriculumRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curriculum not found: " + id));
        model.addAttribute("plan", plan);
        return "curriculum/curriculum"; // template: curriculum.html
    }

    // --- Form: Add Teaching Unit ---
    @GetMapping("/{id}/add-unit")
    public String showAddUnitForm(@PathVariable String id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("teachingUnitDto", new TeachingUnitDto("", "", 0.0, 0.0,true));
        return "curriculum/add_unit"; // template: add_unit.html
    }

    @PostMapping("/{id}/add-unit")
    public String addUnit(@PathVariable String id,
                          @ModelAttribute("teachingUnitDto") TeachingUnitDto dto) {
        service.addTeachingUnit(dto);
        return "redirect:/curriculum/" + id;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("dto", new CurriculumPlanDto("2024/2025"));
        return "curriculum/create_curriculum";
    }

    @PostMapping("/create")
    public String createCurriculum(@ModelAttribute CurriculumPlanDto dto) {
        service.createCurriculumPlan(dto);
        return "redirect:/curriculum/1";
    }


}
