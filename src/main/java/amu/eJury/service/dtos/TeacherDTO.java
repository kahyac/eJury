package amu.eJury.service.dtos;

import java.util.List;

public record TeacherDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        List<Long> ueIds
) {}
