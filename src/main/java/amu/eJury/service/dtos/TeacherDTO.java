package amu.eJury.service.dtos;

public record TeacherDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long ueId
) {}
