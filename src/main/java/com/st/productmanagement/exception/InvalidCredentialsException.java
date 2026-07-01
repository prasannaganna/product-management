package com.st.productmanagement.exception;

public class InvalidCredentialsException extends RuntimeException{
    public  InvalidCredentialsException(String message){
        super(message);
    }
}
