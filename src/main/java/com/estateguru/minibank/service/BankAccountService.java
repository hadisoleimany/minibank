package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.Currency;
import com.estateguru.minibank.model.Customer;
import com.estateguru.minibank.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


public interface BankAccountService {
    void deposit(User user, BigDecimal amount);
    void withdrawal(User user, BigDecimal amount);
    void getBalance(User user, BigDecimal amount);
    Optional<BankAccount> getBankAccount(BankAccountDto dto);
    BankAccount getBankAccountById(Long id);
    BankAccount save(BankAccount account);
    BankAccount update(BankAccount account);
    BankAccount  getBankAccountByAccountNumber(String accountNumber);
    boolean existAccountNumber(String accountNumber);
    boolean existAccountWithCustomerWithCurrency(Customer customer, Currency currency);


}
