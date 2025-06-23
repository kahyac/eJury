package amu.eJury.controller;

import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.service.api.TeachingUnitService;
import amu.eJury.service.dtos.TeachingUnitDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class TeachingUnitController {

    private final TeachingUnitService teachingUnitService;

    @GetMapping("/{id}/add-unit")
    public String showAddUnitForm(@PathVariable String id, Model model) {
        model.addAttribute("curriculumId", id);
        model.addAttribute("teachingUnitDto", new TeachingUnitDto("", "", 1,0.0, 0.0,true));
        return "teachingUnit/add_unit";
    }

    @PostMapping("/{id}/add-unit")
    public String addUnit(@PathVariable String id,
                          @Valid @ModelAttribute("teachingUnitDto") TeachingUnitDto dto,
                          BindingResult result,
                          Model model) {
        model.addAttribute("curriculumId", id);

        if (result.hasErrors()) {
            return "teachingUnit/add_unit";
        }

        try {
            teachingUnitService.addTeachingUnit(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "teachingUnit/add_unit";
        }

        return "redirect:/curriculum/1";
    }

    @GetMapping("/unit/{unitId}/edit")
    public String showEditTeachingUnitForm(@PathVariable Long unitId, Model model) {
        TeachingUnit unit = teachingUnitService.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("UE introuvable : " + unitId));
        model.addAttribute("unitId", unit.getId());
        model.addAttribute("teachingUnitDto", new TeachingUnitDto(
                unit.getCode(), unit.getLabel(), unit.getSemester(), unit.getEcts(), unit.getWorkloadHours(), unit.isObligation()
        ));
        return "teachingUnit/edit_unit";
    }

    @PostMapping("/unit/{unitId}/edit")
    public String updateTeachingUnit(@PathVariable Long unitId,
                                     @Valid @ModelAttribute("teachingUnitDto") TeachingUnitDto dto,
                                     BindingResult result,
                                     Model model) {

        model.addAttribute("unitId", unitId);

        if (result.hasErrors()) {
            return "teachingUnit/edit_unit";
        }

        try {
            teachingUnitService.updateTeachingUnit(unitId, dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("teachingUnitDto", dto);
            return "teachingUnit/edit_unit";
        }

        return "redirect:/curriculum/1";
    }

    @PostMapping("/unit/{unitId}/delete")
    public String deleteTeachingUnit(@PathVariable Long unitId) {
        teachingUnitService.deleteById(unitId);
        return "redirect:/curriculum/1";
    }

}
