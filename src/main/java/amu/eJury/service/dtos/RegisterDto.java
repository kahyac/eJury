package amu.eJury.service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO d'inscription sous forme de record Java.
 */
public record RegisterDto(
        @NotBlank @Size(min = 2, max = 50) String lastName,
        @NotBlank @Size(min = 2, max = 50) String firstName,
        @NotBlank @Email String email,
        @NotBlank String role,
        @NotBlank @Size(min = 8, max = 64) String password
) {
    /** Constructeur par défaut pour Thymeleaf */
    public RegisterDto() {
        this("", "", "", "", "");
    }
}

