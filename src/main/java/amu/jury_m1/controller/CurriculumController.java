package amu.jury_m1.controller;

import amu.jury_m1.dao.AnnualKnowledgeBlockRepository;
import amu.jury_m1.dao.TeachingUnitRepository;
import amu.jury_m1.model.pedagogy.AnnualKnowledgeBlock;
import amu.jury_m1.model.pedagogy.CurriculumPlan;
import amu.jury_m1.service.curriculum.CurriculumManagementService;
import amu.jury_m1.service.dtos.*;
import amu.jury_m1.dao.CurriculumPlanRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Web controller for managing the curriculum through Thymeleaf views.
 * Handles Teaching Units, Knowledge Blocks, and Associations.
 */
@Controller
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class CurriculumController {

    private final CurriculumPlanRepository curriculumRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final AnnualKnowledgeBlockRepository annualKnowledgeBlockRepository;
    private final CurriculumManagementService service;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        boolean exists = curriculumRepository.findById(1L).isPresent();
        model.addAttribute("dto", new CurriculumPlanDto("", ""));
        model.addAttribute("curriculumExists", exists);
        return "curriculum/create_curriculum";
    }

    @PostMapping("/create")
    public String createCurriculum(@Valid @ModelAttribute("dto") CurriculumPlanDto dto,
                                   BindingResult result,
                                   Model model) {
        boolean exists = curriculumRepository.findById(1L).isPresent();
        model.addAttribute("curriculumExists", exists);

        if (result.hasErrors()) {
            return "curriculum/create_curriculum";
        }

        service.createCurriculumPlan(dto);
        return "redirect:/curriculum/1";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        CurriculumPlan plan = curriculumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquette introuvable"));

        CurriculumPlanDto dto = new CurriculumPlanDto(plan.getAcademicYear(), plan.getName());
        model.addAttribute("dto", dto);
        model.addAttribute("planId", plan.getId());
        return "curriculum/edit_curriculum";
    }

    @PostMapping("/{id}/edit")
    public String updateCurriculum(@PathVariable Long id,
                                   @Valid @ModelAttribute("dto") CurriculumPlanDto dto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute("planId", id);
            return "curriculum/edit_curriculum";
        }

        CurriculumPlan plan = curriculumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquette introuvable"));

        plan.setAcademicYear(dto.academicYear());
        plan.setName(dto.name());
        curriculumRepository.save(plan);

        return "redirect:/curriculum/" + id;
    }

    @GetMapping("/{id}")
    public String viewCurriculum(@PathVariable Long id, Model model) {
        CurriculumPlan plan = curriculumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curriculum not found: " + id));
        model.addAttribute("plan", plan);
        model.addAttribute("teachingUnits", teachingUnitRepository.findAll());

        return "curriculum/curriculum";
    }

    @GetMapping("/{id}/add-annual-block")
    public String showAddAnnualBlockForm(@PathVariable Long id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("annualBlockDto", new AnnualKnowledgeBlockDto("")); // simple DTO
        return "curriculum/add_annual_block";
    }

    @PostMapping("/{id}/add-annual-block")
    public String addAnnualBlock(@PathVariable Long id,
                                 @Valid @ModelAttribute("annualBlockDto") AnnualKnowledgeBlockDto dto,
                                 BindingResult result,
                                 Model model) {
        model.addAttribute("curriculumId", id);
        if (result.hasErrors()) {
            return "curriculum/add_annual_block";
        }

        try {
            service.addAnnualKnowledgeBlock(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("curriculumId", id);
            model.addAttribute("annualBlockDto", dto);
            result.rejectValue("id", "duplicate", ex.getMessage());
            return "curriculum/add_annual_block";
        }

        return "redirect:/curriculum/" + id;
    }

    @GetMapping("/annual/{oldId}/rename")
    public String showRenameAnnualBlockForm(@PathVariable String oldId, Model model) {
        model.addAttribute("oldId", oldId);
        model.addAttribute("annualBlockDto", new AnnualKnowledgeBlockDto(""));
        return "curriculum/rename_annual_block";
    }

    @PostMapping("/annual/{oldId}/rename")
    public String renameAnnualBlock(@PathVariable String oldId,
                                    @Valid @ModelAttribute("annualBlockDto") AnnualKnowledgeBlockDto dto,
                                    BindingResult result,
                                    Model model) {
        model.addAttribute("oldId", oldId);

        if (result.hasErrors()) {
            return "curriculum/rename_annual_block";
        }

        try {
            service.renameAnnualBlock(oldId, dto.id());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "curriculum/rename_annual_block";
        }

        return "redirect:/curriculum/1";
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
        model.addAttribute("unitCodes", teachingUnitRepository.findAll()); // <- ici on corrige !
        model.addAttribute("form", new UnitAssociationFormDto("", 1.0));
        Long curriculumId = service.findCurriculumIdBySemBlockCode(blockCode);
        model.addAttribute("curriculumId", curriculumId);
        return "curriculum/add_association";
    }


    @GetMapping("/{id}/add-unit")
    public String showAddUnitForm(@PathVariable String id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("teachingUnitDto", new TeachingUnitDto("", "", 0.0, 0.0,true));
        return "curriculum/add_unit"; // template: add_unit.html
    }

    @PostMapping("/{id}/add-unit")
    public String addUnit(@PathVariable String id,
                          @ModelAttribute("teachingUnitDto") TeachingUnitDto dto,
                          Model model) {
        try {
            service.addTeachingUnit(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("curriculumId", id);
            model.addAttribute("teachingUnitDto", dto);
            model.addAttribute("errorMessage", ex.getMessage());
            return "curriculum/add_unit";
        }

        return "redirect:/curriculum/" + id;
    }

    @PostMapping("/sem-block/{blockCode}/add-unit")
    public String associateUnit(@PathVariable String blockCode,
                                @ModelAttribute("form") UnitAssociationFormDto form,
                                Model model) {
        try {
            service.associateTeachingUnitToSemestrialBlock(blockCode, form.unitCode(), form.coefficient());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("blockCode", blockCode);
            model.addAttribute("units", teachingUnitRepository.findAll());
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", ex.getMessage());
            return "curriculum/add_association";
        }

        return "redirect:/curriculum/1";
    }

    @GetMapping("/annual/{annualId}/edit")
    public String editAnnualBlock(@PathVariable String annualId, Model model) {
        AnnualKnowledgeBlock block = annualKnowledgeBlockRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc annuel introuvable : " + annualId));

        CurriculumPlan plan = block.getCurriculumPlan();

        if (plan == null) {
            throw new IllegalStateException("Ce bloc annuel n'est rattaché à aucun plan de maquette.");
        }

        model.addAttribute("annualBlock", block);
        model.addAttribute("curriculumId", plan.getId());

        return "curriculum/annual_block_edit";
    }

    @PostMapping("/annual/{annualId}/update-id")
    public String updateAnnualBlockId(@PathVariable String annualId,
                                      @RequestParam String newId) {
        AnnualKnowledgeBlock block = annualKnowledgeBlockRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Bloc non trouvé : " + annualId));

        // Changer l'ID (si différent)
        if (!block.getId().equals(newId)) {
            annualKnowledgeBlockRepository.deleteById(annualId); // Supprimer l'ancien
            block.setId(newId);
            annualKnowledgeBlockRepository.save(block); // Sauvegarder sous nouvel ID
        }

        return "redirect:/curriculum/annual/" + newId + "/edit";
    }
}
