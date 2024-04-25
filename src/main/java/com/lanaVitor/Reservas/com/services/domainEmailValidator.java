package com.lanaVitor.Reservas.com.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class domainEmailValidator {

    public static boolean isValid(String email) {

        // Extrair o domínio do endereço de e-mail
        String domain = email.substring(email.indexOf("@") + 1);

        // Configurar as propriedades para a busca do MX record
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp." + domain);

        List<String> acceptableDomains = new ArrayList<>();
        acceptableDomains.add("gmail.com");
        acceptableDomains.add("hotmail.com");
        acceptableDomains.add("yahoo.com");
        acceptableDomains.add("outlook.com");
        acceptableDomains.add("icloud.com");
        acceptableDomains.add("gmail.com.br");
        acceptableDomains.add("hotmail.com.br");
        acceptableDomains.add("yahoo.com.br");
        acceptableDomains.add("outlook.com.br");
        acceptableDomains.add("icloud.com.br");

        return acceptableDomains.contains(domain);
    }
}
