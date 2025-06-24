package amu.eJury.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendNewUserEmail(String to, String password) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Votre compte eJury");
        msg.setText("Bonjour,\n\nVotre compte a été créé.\nMot de passe temporaire : " + password +
                "\nMerci de changer votre mot de passe à la première connexion.\n\nCordialement.");
        mailSender.send(msg);
    }
}
