package amu.eJury.service.dtos;

public record StudentUserDto(
        Long id,
        String firstName,
        String lastName,
        String email
) {}
