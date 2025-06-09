package amu.jury_m1.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UnitAssociationFormDto(
        @NotBlank(message = "Block code cannot be blank")
        String unitCode,

        @PositiveOrZero(message = "Coefficient must be positive or 0")
        double coefficient) {}
