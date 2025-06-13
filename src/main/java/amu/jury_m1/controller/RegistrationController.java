package amu.jury_m1.controller;

import amu.jury_m1.dao.PedagogicalRegistrationRepository;
import amu.jury_m1.model.registration.PedagogicalRegistration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {
    private final PedagogicalRegistrationRepository registrationRepository;

    public RegistrationController(PedagogicalRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @GetMapping
    @ResponseBody
    public List<PedagogicalRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}