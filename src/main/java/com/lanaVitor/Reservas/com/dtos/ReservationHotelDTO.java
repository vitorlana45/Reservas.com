package com.lanaVitor.Reservas.com.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationHotelDTO {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long numberRoom ;

}
