package amu.eJury.service.dtos;
/**
 * DTO de connexion sous forme de record Java.
 * Inclut un constructeur sans-argument pour Thymeleaf.
 */
public record LoginDTO(
        String username,
        String password,
        boolean rememberMe
) {
    /**
     * Constructeur par défaut pour faciliter le binding Thymeleaf.
     */
    public LoginDTO() {
        this("", "", false);
    }
}
