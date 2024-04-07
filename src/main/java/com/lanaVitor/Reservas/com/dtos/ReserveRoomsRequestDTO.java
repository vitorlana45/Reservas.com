package com.lanaVitor.Reservas.com.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReserveRoomsRequestDTO {
    private ReservationHotelDTO reservationDTO;
    private VerificationRegisterDTO user;
}
