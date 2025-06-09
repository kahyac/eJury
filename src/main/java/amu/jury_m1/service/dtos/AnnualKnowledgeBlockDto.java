package amu.jury_m1.service.dtos;

import jakarta.validation.constraints.NotBlank;

public record AnnualKnowledgeBlockDto(

        @NotBlank(message = "ID cannot be blank")
        String id

) {}