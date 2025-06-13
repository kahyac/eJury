package amu.jury_m1.controller;

import amu.jury_m1.service.api.SemestrialKnowledgeBlockService;
import amu.jury_m1.service.api.TeachingUnitService;
import amu.jury_m1.service.dtos.SemestrialKnowledgeBlockDto;
import amu.jury_m1.service.dtos.UnitAssociationFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class SemestrialBlockController {

    private final SemestrialKnowledgeBlockService semestrialKnowledgeBlockService;
    private final TeachingUnitService teachingUnitService;

    @GetMapping("/annual/{annualId}/add-sem-block")
    public String showAddSemBlockForm(@PathVariable String annualId, Model model) {
        model.addAttribute("annualId", annualId);
        model.addAttribute("semestrialDto", new SemestrialKnowledgeBlockDto("", "", 1, 0.0));
        return "semestrialKnowledgeBlock/add_sem_block";
    }

    @PostMapping("/annual/{annualId}/add-sem-block")
    public String addSemBlock(@PathVariable String annualId,
                              @ModelAttribute SemestrialKnowledgeBlockDto dto) {
        semestrialKnowledgeBlockService.addSemestrialBlockToAnnual(annualId, dto);
        return "redirect:/curriculum/annual/" + annualId + "/edit";
    }

    @GetMapping("/sem-block/{blockCode}/add-unit")
    public String showAddUnitToBlock(@PathVariable String blockCode, Model model) {
        model.addAttribute("blockCode", blockCode);
        model.addAttribute("unitCodes", teachingUnitService.findAll());
        model.addAttribute("form", new UnitAssociationFormDto("", 1.0));

        String annualId = semestrialKnowledgeBlockService.findAnnualIdBySemBlockCode(blockCode);

        model.addAttribute("annualId", annualId);

        return "semestrialKnowledgeBlock/add_association";
    }

    @PostMapping("/sem-block/{blockCode}/add-unit")
    public String associateUnit(@PathVariable String blockCode,
                                @ModelAttribute("form") UnitAssociationFormDto form,
                                Model model) {
        try {
            semestrialKnowledgeBlockService.associateTeachingUnitToSemestrialBlock(blockCode, form.unitCode(), form.coefficient());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("blockCode", blockCode);
            model.addAttribute("units", teachingUnitService.findAll());
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", ex.getMessage());
            return "semestrialKnowledgeBlock/add_association";
        }

        return "redirect:/curriculum/1";
    }
}
