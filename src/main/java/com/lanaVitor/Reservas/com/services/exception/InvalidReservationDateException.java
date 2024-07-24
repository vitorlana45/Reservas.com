package com.lanaVitor.Reservas.com.services.exception;

public class InvalidReservationDateException extends RuntimeException {

    public InvalidReservationDateException (String msg){
        super(msg);
    }
}
