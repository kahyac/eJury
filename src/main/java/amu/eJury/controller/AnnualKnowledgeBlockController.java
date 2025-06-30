package amu.eJury.controller;

import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.service.api.AnnualKnowledgeBlockService;
import amu.eJury.service.dtos.AnnualKnowledgeBlockDTO;
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
        model.addAttribute("annualBlockDto", new AnnualKnowledgeBlockDTO(""));
        return "annualKnowledgeBlock/add_annual_block";
    }

    @PostMapping("/{id}/add-annual-block")
    public String addAnnualBlock(@PathVariable Long id,
                                 @Valid @ModelAttribute("annualBlockDto") AnnualKnowledgeBlockDTO dto,
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
            result.rejectValue("code", "duplicate", ex.getMessage());
            return "annualKnowledgeBlock/add_annual_block";
        }

        return "redirect:/curriculum/" + id;
    }

    @GetMapping("/annual/{annualId}/rename")
    public String showRenameAnnualBlockForm(@PathVariable Long annualId, Model model) {
        AnnualKnowledgeBlock block = annualKnowledgeBlockService.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc non trouvé : " + annualId));
        model.addAttribute("oldCode", block.getCode());
        model.addAttribute("annualId", annualId);
        model.addAttribute("annualBlockDto", new AnnualKnowledgeBlockDTO(""));
        return "annualKnowledgeBlock/rename_annual_block";
    }

    @PostMapping("/annual/{annualId}/rename")
    public String renameAnnualBlock(@PathVariable Long annualId,
                                    @Valid @ModelAttribute("annualBlockDto") AnnualKnowledgeBlockDTO dto,
                                    BindingResult result,
                                    Model model) {

        if (result.hasErrors()) {
            return "annualKnowledgeBlock/rename_annual_block";
        }

        try {
            annualKnowledgeBlockService.renameAnnualBlock(annualId, dto.code());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "annualKnowledgeBlock/rename_annual_block";
        }

        return "redirect:/curriculum/1";
    }

    @GetMapping("/annual/{annualId}/edit")
    public String editAnnualBlock(@PathVariable Long annualId, Model model) {
        AnnualKnowledgeBlock block = annualKnowledgeBlockService.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc annuel introuvable : " + annualId));

        model.addAttribute("annualBlock", block);
        model.addAttribute("curriculumId", block.getCurriculumPlan().getId());

        return "annualKnowledgeBlock/annual_block_edit";
    }

    @PostMapping("/annual/{annualId}/delete")
    public String deleteAnnualBlock(@PathVariable Long annualId) {
        AnnualKnowledgeBlock block = annualKnowledgeBlockService.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc introuvable"));
        Long curriculumId = block.getCurriculumPlan().getId();
        annualKnowledgeBlockService.deleteById(annualId);
        return "redirect:/curriculum/" + curriculumId;
    }
}
