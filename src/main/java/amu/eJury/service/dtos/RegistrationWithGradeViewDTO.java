package amu.eJury.service.dtos;

import amu.eJury.model.registration.PedagogicalRegistration;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationWithGradeViewDTO {
    private PedagogicalRegistration registration;
    private String displayNote;
}
