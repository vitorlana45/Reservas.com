package com.lanaVitor.Reservas.com.services.exception;

import java.io.Serial;

public class NullEntityException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public NullEntityException(String msg) {
        super(msg);
    }
}
