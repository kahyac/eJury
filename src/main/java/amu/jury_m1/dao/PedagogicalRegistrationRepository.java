package amu.jury_m1.dao;


import amu.jury_m1.model.registration.PedagogicalRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedagogicalRegistrationRepository extends JpaRepository<PedagogicalRegistration, String> {
}