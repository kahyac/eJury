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

    public void sendResetPasswordLink(String to, String resetLink) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Réinitialisation de votre mot de passe – eJury");
        msg.setText("Bonjour,\n\nCliquez sur le lien suivant pour réinitialiser votre mot de passe :\n"
                + resetLink + "\n\nCe lien est valable pendant 1 heure.\n\nCordialement.");
        mailSender.send(msg);
    }

}
