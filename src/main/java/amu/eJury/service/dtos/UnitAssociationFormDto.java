package amu.eJury.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UnitAssociationFormDto(
        @NotBlank(message = "Block code cannot be blank")
        Long unitId,

        @PositiveOrZero(message = "Coefficient must be positive or 0")
        double coefficient) {}
