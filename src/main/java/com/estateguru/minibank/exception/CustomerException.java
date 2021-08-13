package com.estateguru.minibank.exception;

public class CustomerException extends BusinessException{
    public CustomerException(String message) {
        super(message);
    }

    public CustomerException(String message, String customerCode) {
        super(message+"  Code : "+customerCode);
    }
}
