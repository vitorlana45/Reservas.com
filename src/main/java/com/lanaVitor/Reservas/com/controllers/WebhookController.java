package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin({"https://reservas-five.vercel.app","https://reservas-44eh.onrender.com"})
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final String FIELD_REQUEST_HANDLER = "Stripe-Signature";
    private final WebhookService webhookService;

    @Autowired
    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/api/checkout")
    public void handleWebhook(@RequestBody String payload, @RequestHeader(name = FIELD_REQUEST_HANDLER) String sigHeader) {

        webhookService.creatingEventAndCheckCase(payload, sigHeader);
    }
}
