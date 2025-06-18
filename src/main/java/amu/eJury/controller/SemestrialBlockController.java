package amu.eJury.controller;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.service.api.SemestrialKnowledgeBlockService;
import amu.eJury.service.api.TeachingUnitService;
import amu.eJury.service.dtos.SemestrialKnowledgeBlockDto;
import amu.eJury.service.dtos.UnitAssociationFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class SemestrialBlockController {

    private final SemestrialKnowledgeBlockService semestrialKnowledgeBlockService;
    private final TeachingUnitService teachingUnitService;

    @GetMapping("/annual/{annualId}/add-sem-block")
    public String showAddSemBlockForm(@PathVariable Long annualId, Model model) {
        model.addAttribute("annualId", annualId);
        model.addAttribute("semestrialDto", new SemestrialKnowledgeBlockDto("", "", 1, 0.0));
        return "semestrialKnowledgeBlock/add_sem_block";
    }

    @PostMapping("/annual/{annualId}/add-sem-block")
    public String addSemBlock(@PathVariable Long annualId,
                              @ModelAttribute SemestrialKnowledgeBlockDto dto) {
        semestrialKnowledgeBlockService.addSemestrialBlockToAnnual(annualId, dto);
        return "redirect:/curriculum/annual/" + annualId + "/edit";
    }

    @GetMapping("/sem-block/{blockId}/add-unit")
    public String showAddUnitToBlock(@PathVariable Long blockId, Model model) {
        SemestrialKnowledgeBlock block = semestrialKnowledgeBlockService.findById(blockId);

        model.addAttribute("blockId", block.getId());
        model.addAttribute("blockCode", block.getCode());
        model.addAttribute("unitCodes", teachingUnitService.findAll());
        model.addAttribute("form", new UnitAssociationFormDto(null, 1.0));

        Long annualId = semestrialKnowledgeBlockService.findAnnualIdBySemBlockId(blockId);

        model.addAttribute("annualId", annualId);

        return "semestrialKnowledgeBlock/add_association";
    }

    @PostMapping("/sem-block/{blockId}/add-unit")
    public String associateUnit(@PathVariable Long blockId,
                                @ModelAttribute("form") UnitAssociationFormDto form,
                                Model model) {
        try {
            semestrialKnowledgeBlockService.associateTeachingUnitToSemestrialBlock(blockId, form.unitId(), form.coefficient());
        } catch (IllegalArgumentException ex) {
            SemestrialKnowledgeBlock block = semestrialKnowledgeBlockService.findById(blockId);
            model.addAttribute("blockId", block.getId());
            model.addAttribute("blockCode",block.getCode());
            model.addAttribute("units", teachingUnitService.findAll());
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", ex.getMessage());
            return "semestrialKnowledgeBlock/add_association";
        }

        return "redirect:/curriculum/1";
    }

    @GetMapping("/sem-block/{blockId}/edit")
    public String showEditForm(@PathVariable Long blockId, Model model) {
        SemestrialKnowledgeBlock block = semestrialKnowledgeBlockService.findById(blockId);
        SemestrialKnowledgeBlockDto dto = new SemestrialKnowledgeBlockDto(
                block.getCode(), block.getLabel(), block.getSemester(), block.getEcts()
        );

        Long annualId = semestrialKnowledgeBlockService.findAnnualIdBySemBlockId(blockId);

        model.addAttribute("blockDto", dto);
        model.addAttribute("block", block);
        model.addAttribute("annualId", annualId);

        return "semestrialKnowledgeBlock/edit_sem_block";
    }


    @PostMapping("/sem-block/{blockId}/edit")
    public String updateSemBlock(@PathVariable Long blockId,
                                 @ModelAttribute SemestrialKnowledgeBlockDto dto) {
        semestrialKnowledgeBlockService.updateSemestrialBlock(blockId, dto);
        Long annualId = semestrialKnowledgeBlockService.findAnnualIdBySemBlockId(blockId);
        return "redirect:/curriculum/annual/" + annualId + "/edit";
    }


    @PostMapping("/sem-block/{blockId}/delete")
    public String deleteSemBlock(@PathVariable Long blockId) {
        Long annualId = semestrialKnowledgeBlockService.findAnnualIdBySemBlockId(blockId);
        semestrialKnowledgeBlockService.deleteById(blockId);
        return "redirect:/curriculum/annual/" + annualId + "/edit";
    }

    @PostMapping("/association/{semBlockId}/{unitId}/delete")
    public String deleteUnitAssociation(@PathVariable Long semBlockId,
                                        @PathVariable Long unitId,
                                        RedirectAttributes redirectAttributes) {
        semestrialKnowledgeBlockService.removeUnitFromSemestrialBlock(semBlockId, unitId);
        redirectAttributes.addFlashAttribute("success", "Association supprimée.");
        return "redirect:/curriculum/annual/" + semestrialKnowledgeBlockService.findAnnualIdBySemBlockId(semBlockId) + "/edit";
    }

}
