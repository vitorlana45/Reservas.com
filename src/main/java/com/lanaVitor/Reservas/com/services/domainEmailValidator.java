package com.lanaVitor.Reservas.com.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class domainEmailValidator {

    private static final List<String> acceptableDomains;

    static {
        acceptableDomains = new ArrayList<>();
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
    }

    public static boolean isValid(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        return acceptableDomains.contains(domain);
    }
}
