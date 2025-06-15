package amu.jury_m1.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AnnualKnowledgeBlockDto(

        @NotBlank(message = "Le nom ne doit pas être vide.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "Le nom est invalide : il ne peut contenir que des espaces.")
        String code

) {}