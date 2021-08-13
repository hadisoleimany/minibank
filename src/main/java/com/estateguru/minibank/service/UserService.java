package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.model.BankAccount;

import java.math.BigDecimal;


public interface UserService {
    BankAccount createAccount(CustomerDto customerDto, String accountNumber, String currencyCode,BigDecimal firstDeposit);
    BankAccount depositAccount(BankAccountDto accountDto,BigDecimal amount);
    BankAccount withdrawalAccount( BankAccountDto accountDto, BigDecimal amount);
    BankAccount transfer(BankAccountDto fromAccount,BankAccountDto toAccount,BigDecimal amount);

}
