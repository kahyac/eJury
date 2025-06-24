package amu.eJury.service.impl;

import amu.eJury.dao.AppUserRepository;
import amu.eJury.model.users.AppUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FirstLoginRedirectHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AppUserRepository appUserRepository;

    public FirstLoginRedirectHandler(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow();

        if (user.isFirstLogin()) {
            getRedirectStrategy().sendRedirect(request, response, "/change-password");
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
