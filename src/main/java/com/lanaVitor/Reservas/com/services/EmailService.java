package com.lanaVitor.Reservas.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private JavaMailSender javaMailSender;
    @Autowired
    public EmailService (JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }


    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendEmailText(String user, String subject, String message) {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender); // Usando o endereço configurado no application.properties
            simpleMailMessage.setTo(user); // Definindo o destinatário como o e-mail do usuário
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);

    }
}
