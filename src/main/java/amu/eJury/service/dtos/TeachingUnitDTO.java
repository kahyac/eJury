package amu.eJury.service.dtos;

import jakarta.validation.constraints.*;


public record TeachingUnitDTO(

        @NotBlank(message = "Le code de l'UE est obligatoire.")
        String code,

        @NotBlank(message = "Le libellé est obligatoire.")
        String label,

        @NotBlank
        @Min(1)
        @Max(2)
        int semester,

        @Min(value = 1, message = "Les ECTS doivent être au moins de 1.")
        @Max(value = 30, message = "Les ECTS ne peuvent dépasser 30.")
        double ects,

        @Min(value = 1, message = "Le volume horaire doit être au moins de 1h.")
        @Max(value = 300, message = "Le volume horaire ne peut dépasser 300h.")
        double workloadHours,

        boolean obligation
) {}