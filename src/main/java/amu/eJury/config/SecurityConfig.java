package amu.eJury.config;

import amu.eJury.service.impl.FirstLoginRedirectHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, FirstLoginRedirectHandler successHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/reset-password", "/reset-password/confirm", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                        .requestMatchers("/").authenticated()
                        .requestMatchers("/curriculum/create").hasRole("ADMIN")
                        .requestMatchers("/curriculum/1").hasRole("ADMIN")
                        .requestMatchers("/registrations").hasRole("ADMIN")
                        .requestMatchers("/jury/run").hasRole("ADMIN")
                        .requestMatchers("/grades/new").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/results/view").hasAnyRole("STUDENT","ADMIN")
                        .requestMatchers("/grades/new", "/grades/save").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/students/**").hasRole("ADMIN")
                        .requestMatchers("/teachers/**").hasRole("ADMIN")
                        .requestMatchers("/grades/pv/download").hasAnyRole("ADMIN", "STUDENT")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }
}
