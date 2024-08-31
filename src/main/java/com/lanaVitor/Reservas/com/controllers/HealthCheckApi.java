package com.lanaVitor.Reservas.com.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = {"http://localhost:4200", "https://reservas-44eh.onrender.com"})
public class HealthCheckApi {

    @GetMapping("/status")
    public ResponseEntity<String> statusApi() {
        return ResponseEntity.ok("{status:'UP'}");
    }
}
