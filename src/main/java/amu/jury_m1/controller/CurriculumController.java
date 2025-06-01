package amu.jury_m1.controller;

import amu.jury_m1.domain.pedagogy.CurriculumPlan;
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
    private final CurriculumManagementService service;

    // --- Display curriculum details ---
    @GetMapping("/{id}")
    public String viewCurriculum(@PathVariable String id, Model model) {
        CurriculumPlan plan = curriculumRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curriculum not found: " + id));
        model.addAttribute("plan", plan);
        return "curriculum"; // template: curriculum.html
    }

    // --- Form: Add Teaching Unit ---
    @GetMapping("/{id}/add-unit")
    public String showAddUnitForm(@PathVariable String id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("teachingUnitDto", new TeachingUnitDto("", "", 0.0, 0.0));
        return "add_unit"; // template: add_unit.html
    }

    @PostMapping("/{id}/add-unit")
    public String addUnit(@PathVariable String id,
                          @ModelAttribute TeachingUnitDto dto) {
        service.addTeachingUnit(id, dto);
        return "redirect:/curriculum/" + id;
    }

    // --- Form: Add Knowledge Block ---
    @GetMapping("/{id}/add-block")
    public String showAddBlockForm(@PathVariable String id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("knowledgeBlockDto", new KnowledgeBlockDto("", "", 1, 0.0));
        return "add_block"; // template: add_block.html
    }

    @PostMapping("/{id}/add-block")
    public String addBlock(@PathVariable String id,
                           @ModelAttribute KnowledgeBlockDto dto) {
        service.addKnowledgeBlock(id, dto);
        return "redirect:/curriculum/" + id;
    }

    // --- Form: Associate Teaching Unit to Block ---
    @GetMapping("/{id}/add-association")
    public String showAddAssociationForm(@PathVariable String id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("associationDto", new UnitInBlockAssociationDto("", "", 1.0));
        return "add_association"; // template: add_association.html
    }

    @PostMapping("/{id}/add-association")
    public String addAssociation(@PathVariable String id,
                                 @ModelAttribute UnitInBlockAssociationDto dto) {
        service.associateUnitToBlock(dto, id);
        return "redirect:/curriculum/" + id;
    }
}
