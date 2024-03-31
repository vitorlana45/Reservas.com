package com.lanaVitor.Reservas.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public String sendEmailText(String user, String assunto, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(remetente); // Usando o endereço configurado no application.properties
            simpleMailMessage.setTo(user); // Definindo o destinatário como o e-mail do usuário
            simpleMailMessage.setSubject(assunto);
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao enviar email";
        }
    }
}
