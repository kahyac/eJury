package amu.eJury.service.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TeachingUnitGradeForm(

        @NotNull
        Long studentId,

        @NotNull
        Long unitId,

        @NotNull
        String status,  // "NONE", "ABI", "ABJ", "AR"

        @Min(0)
        @Max(20)
        Double numeric // null sauf si status = "NONE"
) {}