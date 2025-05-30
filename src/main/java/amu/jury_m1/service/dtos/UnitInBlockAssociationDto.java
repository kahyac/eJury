package amu.jury_m1.service.dtos;


import jakarta.validation.constraints.*;

public record UnitInBlockAssociationDto(

        @NotBlank(message = "Teaching Unit code cannot be blank")
        String teachingUnitCode,

        @NotBlank(message = "Block code cannot be blank")
        String blockCode,

        @PositiveOrZero(message = "Coefficient must be positive or 0")
        double coefficient
) {}