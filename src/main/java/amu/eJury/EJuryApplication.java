package amu.eJury;

import amu.eJury.dao.AppUserRepository;
import amu.eJury.model.users.AppUser;
import amu.eJury.model.users.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EJuryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EJuryApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(AppUserRepository appUserRepository, PasswordEncoder encoder) {
        return args -> {
            if (appUserRepository.findByEmail("admin@ejury.fr").isEmpty()) {
                appUserRepository.save(AppUser.builder()
                        .email("admin@ejury.fr")
                        .password(encoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .firstLogin(true)
                        .build());
            }
        };
    }
}
