package com.lanaVitor.Reservas.com.services.exception;

public class noExistsUserException extends RuntimeException{

    public noExistsUserException (String msg){
        super(msg);
    }

}
