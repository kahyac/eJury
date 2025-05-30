package amu.jury_m1.controller;

import amu.jury_m1.service.curriculum.CurriculumManagementService;
import amu.jury_m1.service.dtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curriculums")
@RequiredArgsConstructor
public class CurriculumController {

    private final CurriculumManagementService service;

    // Créer une nouvelle maquette
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCurriculum(@RequestBody @Valid CurriculumPlanDto dto) {
        service.createCurriculumPlan(dto);
    }

    // Ajouter une UE à une maquette
    @PostMapping("/{id}/teaching-units")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTeachingUnit(@PathVariable String id, @RequestBody @Valid TeachingUnitDto dto) {
        service.addTeachingUnit(id, dto);
    }

    // Ajouter un BCC à une maquette
    @PostMapping("/{id}/knowledge-blocks")
    @ResponseStatus(HttpStatus.CREATED)
    public void addKnowledgeBlock(@PathVariable String id, @RequestBody @Valid KnowledgeBlockDto dto) {
        service.addKnowledgeBlock(id, dto);
    }

    // Associer une UE à un BCC dans une maquette
    @PostMapping("/{id}/associations")
    @ResponseStatus(HttpStatus.CREATED)
    public void associateUnitToBlock(@PathVariable String id, @RequestBody @Valid UnitInBlockAssociationDto dto) {
        service.associateUnitToBlock(dto, id);
    }
}
