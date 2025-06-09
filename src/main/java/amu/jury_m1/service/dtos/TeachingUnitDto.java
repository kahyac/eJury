package amu.jury_m1.service.dtos;

import jakarta.validation.constraints.*;


public record TeachingUnitDto(

        @NotBlank(message = "Code cannot be blank")
        String code,

        @NotBlank(message = "Label cannot be blank")
        String label,

        @Positive(message = "ECTS must be positive")
        double ects,

        @Positive(message = "Workload must be positive")
        double workloadHours,

        boolean obligation
) {}