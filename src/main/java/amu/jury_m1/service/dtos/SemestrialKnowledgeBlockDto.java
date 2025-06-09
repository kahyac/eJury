package amu.jury_m1.service.dtos;

import jakarta.validation.constraints.*;


public record SemestrialKnowledgeBlockDto(

        @NotBlank(message = "Code cannot be blank")
        String code,

        @NotBlank(message = "Label cannot be blank")
        String label,

        @Min(value = 1, message = "Semester must be 1 or 2")
        @Max(value = 2, message = "Semester must be 1 or 2")
        int semester,

        @Positive(message = "ECTS must be positive")
        double ects
) {}