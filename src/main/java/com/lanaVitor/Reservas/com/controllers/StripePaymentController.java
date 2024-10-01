package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.CheckoutRequest;
import com.lanaVitor.Reservas.com.services.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = {"https://reservas-five.vercel.app", "https://reservas-44eh.onrender.com"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/checkout")
public class StripePaymentController {

    private final StripeService stripeService;

    public StripePaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody CheckoutRequest checkoutRequest) throws StripeException {
        return stripeService.createSession(checkoutRequest);
    }
}