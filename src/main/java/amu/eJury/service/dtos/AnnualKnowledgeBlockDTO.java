package amu.eJury.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AnnualKnowledgeBlockDTO(

        @NotBlank(message = "Le nom ne doit pas être vide.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "Le nom est invalide : il ne peut contenir que des espaces.")
        String code

) {}