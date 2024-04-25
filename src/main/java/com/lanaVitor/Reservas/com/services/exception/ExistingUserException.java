package com.lanaVitor.Reservas.com.services.exception;

public class ExistingUserException extends RuntimeException{

    public ExistingUserException (String msg){
        super(msg);
    }

}
