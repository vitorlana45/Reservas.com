package com.lanaVitor.Reservas.com.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationHotelDTO {
    private Date checkIn;
    private Date checkOut;
    private Long numberRoom ;

}
