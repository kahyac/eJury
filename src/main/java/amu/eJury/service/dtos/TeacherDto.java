package amu.eJury.service.dtos;

public record TeacherDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long ueId
) {}
