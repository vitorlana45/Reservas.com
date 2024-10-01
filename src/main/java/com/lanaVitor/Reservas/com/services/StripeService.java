package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.CheckoutRequest;
import com.lanaVitor.Reservas.com.services.util.DateFormatter;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${my-secret-key-stripe}")
    private String secretKey;

    @PostConstruct
    public void init() {
        com.stripe.Stripe.apiKey = secretKey;
    }

    public Map<String, String> createSession(CheckoutRequest checkoutRequest) throws StripeException {
        // criando o client na stripe para setar o email do cliente com o email do usuário logado
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", checkoutRequest.getUserEmail());
        Customer customer = Customer.create(customerParams);

        // Criação da sessão de pagamento, vinculando ao cliente criado
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", new String[]{"card"});
        params.put("mode", "payment");

        // inserindo dados que mandei do front end para a criação da sessão
        Map<String, String> metadata = new HashMap<>();
        metadata.put("check_in", String.valueOf(DateFormatter.formatDateString(checkoutRequest.getCheckIn())));
        metadata.put("check_out", String.valueOf(DateFormatter.formatDateString(checkoutRequest.getCheckOut())));
        metadata.put("quantity_people", String.valueOf(checkoutRequest.getQuantityPeople()));

        Map<String, Object> lineItem = new HashMap<>();
        lineItem.put("price_data", Map.of(
                "currency", "brl",
                "product_data", Map.of(
                        "name", "Reserva de Hospedagem",
                        "description", "Valor por pessoa: R$ 120,00\nValor por dia: R$ 100,00 || " +
                                "CARTÃO DE TESTE: 4242 4242 4242 4242 | CVV: 123 | Validade: 12/25"
                ),
                "unit_amount", checkoutRequest.getAmount()
        ));
        lineItem.put("quantity", 1);

        params.put("line_items", new Object[]{lineItem});
        params.put("success_url", "http://localhost:4200/success");
        params.put("cancel_url", "http://localhost:4200/cancel");
        params.put("customer", customer.getId());
        params.put("metadata", metadata);

        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("checkoutUrl", session.getUrl());
        return response;
    }
}
