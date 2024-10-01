package com.lanaVitor.Reservas.com.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {
    private int amount;
    private String checkIn;
    private String checkOut;
    private int quantityPeople;
    private String userEmail;

}
