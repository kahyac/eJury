package amu.eJury.service.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PedagogicalRegistrationForm(

        @NotNull
        Long studentId,

        @NotNull
        Long teachingUnitId,

        @NotNull
        @Min(1) @Max(2) Integer semester
) {}