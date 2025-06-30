package amu.eJury.service.dtos;

public record StudentUserDTO(
        Long id,
        String firstName,
        String lastName,
        String email
) {}
