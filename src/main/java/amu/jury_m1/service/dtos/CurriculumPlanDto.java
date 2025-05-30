package amu.jury_m1.service.dtos;

import jakarta.validation.constraints.*;

public record CurriculumPlanDto(
        @NotBlank(message = "ID cannot be blank")
        String id,
        @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "Academic year must be in format YYYY/YYYY")
        String academicYear
) {}