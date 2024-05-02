package com.lanaVitor.Reservas.com.services.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String msg) {
        super(msg);
    }
}
