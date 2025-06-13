package amu.jury_m1.controller;

import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.service.api.AnnualKnowledgeBlockService;
import amu.jury_m1.service.dtos.AnnualKnowledgeBlockDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class AnnualKnowledgeBlockController {

    private final AnnualKnowledgeBlockService annualKnowledgeBlockService;

    @GetMapping("/{id}/add-annual-block")
    public String showAddAnnualBlockForm(@PathVariable Long id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("annualBlockDto", new AnnualKnowledgeBlockDto(""));
        return "annualKnowledgeBlock/add_annual_block";
    }

    @PostMapping("/{id}/add-annual-block")
    public String addAnnualBlock(@PathVariable Long id,
                                 @Valid @ModelAttribute("annualBlockDto") AnnualKnowledgeBlockDto dto,
                                 BindingResult result,
                                 Model model) {
        model.addAttribute("curriculumId", id);
        if (result.hasErrors()) {
            return "annualKnowledgeBlock/add_annual_block";
        }

        try {
            annualKnowledgeBlockService.addAnnualKnowledgeBlock(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("curriculumId", id);
            model.addAttribute("annualBlockDto", dto);
            result.rejectValue("id", "duplicate", ex.getMessage());
            return "annualKnowledgeBlock/add_annual_block";
        }

        return "redirect:/curriculum/" + id;
    }

    @GetMapping("/annual/{oldId}/rename")
    public String showRenameAnnualBlockForm(@PathVariable String oldId, Model model) {
        model.addAttribute("oldId", oldId);
        model.addAttribute("annualBlockDto", new AnnualKnowledgeBlockDto(""));
        return "annualKnowledgeBlock/rename_annual_block";
    }

    @PostMapping("/annual/{oldId}/rename")
    public String renameAnnualBlock(@PathVariable String oldId,
                                    @Valid @ModelAttribute("annualBlockDto") AnnualKnowledgeBlockDto dto,
                                    BindingResult result,
                                    Model model) {
        model.addAttribute("oldId", oldId);

        if (result.hasErrors()) {
            return "annualKnowledgeBlock/rename_annual_block";
        }

        try {
            annualKnowledgeBlockService.renameAnnualBlock(oldId, dto.id());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "annualKnowledgeBlock/rename_annual_block";
        }

        return "redirect:/curriculum/1";
    }

    @GetMapping("/annual/{annualId}/edit")
    public String editAnnualBlock(@PathVariable String annualId, Model model) {
        AnnualKnowledgeBlock block = annualKnowledgeBlockService.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc annuel introuvable : " + annualId));

        CurriculumPlan plan = block.getCurriculumPlan();

        if (plan == null) {
            throw new IllegalStateException("Ce bloc annuel n'est rattaché à aucun plan de maquette.");
        }

        model.addAttribute("annualBlock", block);
        model.addAttribute("curriculumId", plan.getId());

        return "annualKnowledgeBlock/annual_block_edit";
    }

    @PostMapping("/annual/{annualId}/update-id")
    public String updateAnnualBlockId(@PathVariable String annualId,
                                      @RequestParam String newId) {
        annualKnowledgeBlockService.updateAnnualBlockId(annualId, newId);
        return "redirect:/curriculum/annual/" + newId + "/edit";
    }
}
