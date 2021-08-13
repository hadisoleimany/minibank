package com.estateguru.minibank.exception;

public class BankAccountException extends BusinessException {
    public BankAccountException(String message,String accountNumber) {
        super(message+" Account Number is : "+accountNumber);
    }

    public BankAccountException(String message) {
        super(message);
    }
}
