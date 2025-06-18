package amu.eJury.service.dtos;
/**
 * DTO de connexion sous forme de record Java.
 * Inclut un constructeur sans-argument pour Thymeleaf.
 */
public record LoginDto(
        String username,
        String password,
        boolean rememberMe
) {
    /**
     * Constructeur par défaut pour faciliter le binding Thymeleaf.
     */
    public LoginDto() {
        this("", "", false);
    }
}

