package amu.jury_m1.service.dtos;

import jakarta.validation.constraints.*;

public record CurriculumPlanDto(

        @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "Année universitaire invalide : le format doit être AAAA/AAAA.")
        String academicYear,

        @NotBlank(message = "Le nom ne doit pas être vide.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "Le nom est invalide : il ne peut contenir que des espaces.")
        String name

) {}